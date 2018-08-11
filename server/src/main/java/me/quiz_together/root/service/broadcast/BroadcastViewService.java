package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.NotFoundUserException;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.BroadcastReq;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateReq;
import me.quiz_together.root.model.request.broadcast.DeleteBroadcastReq;
import me.quiz_together.root.model.request.broadcast.EndBroadcastReq;
import me.quiz_together.root.model.request.broadcast.LeaveBroadcastReq;
import me.quiz_together.root.model.request.broadcast.SendAnswerReq;
import me.quiz_together.root.model.request.broadcast.StartBroadcastReq;
import me.quiz_together.root.model.request.broadcast.UpdateBroadcastStatusReq;
import me.quiz_together.root.model.request.question.QuestionReq;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.JoinBroadcastView;
import me.quiz_together.root.model.response.broadcast.PagingBroadcastListView;
import me.quiz_together.root.model.response.broadcast.StartBroadcastView;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.model.response.user.UserInfoView;
import me.quiz_together.root.model.user.PlayUserStatus;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.service.FcmService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

@Slf4j
@Service
public class BroadcastViewService {
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private FcmService fcmService;

    public PagingBroadcastListView getPagingBroadcastList(long next, int limit, Long userId) {
        PagingBroadcastListView pagingBroadcastListView = new PagingBroadcastListView();
        if (Objects.nonNull(userId)) {
            List<Broadcast> broadcastList = broadcastService.getMyBroadcastList(userId);
            pagingBroadcastListView.setMyBroadcastList(buildBroadcastViewList(broadcastList));
        }

        List<Broadcast> broadcastList = broadcastService.getPagingBroadcastList(next, limit, userId);
        pagingBroadcastListView.setCurrentBroadcastList(buildBroadcastViewList(broadcastList));

        return pagingBroadcastListView;
    }

    public BroadcastForUpdateView getBroadcastForUpdateById(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);

        if (broadcast.getBroadcastStatus() != BroadcastStatus.CREATED) {
            // update fail
            throw new IllegalArgumentException(broadcast.getBroadcastStatus().name() + "상태에서는 변경할 수 없습니다.");
        }
        List<Question> questionList = questionService.getQuestionListByBroadcastId(broadcastId);

        return BroadcastForUpdateView.builder()
                                     .broadcastId(broadcast.getId())
                                     .description(broadcast.getDescription())
                                     .giftDescription(broadcast.getGiftDescription())
                                     .giftType(broadcast.getGiftType())
                                     .prize(broadcast.getPrize())
                                     .winnerMessage(broadcast.getWinnerMessage())
                                     .scheduledTime(broadcast.getScheduledTime())
                                     .questionList(questionList.stream().map(this::buildQuestionView)
                                                               .collect(Collectors.toList()))
                                     .build();
    }

    public String updateBroadcast(BroadcastUpdateReq broadcastUpdateReq) {
        //TODO: 권한 체크 필요

        Broadcast broadcast = new Broadcast();
        broadcast.setId(broadcastUpdateReq.getBroadcastId());
        broadcast.setDescription(broadcastUpdateReq.getDescription());
        broadcast.setGiftDescription(broadcastUpdateReq.getGiftDescription());
        broadcast.setGiftType(broadcastUpdateReq.getGiftType());
        broadcast.setPrize(broadcastUpdateReq.getPrize());
        broadcast.setQuestionCount(broadcastUpdateReq.getQuestionList().size());
        broadcast.setScheduledTime(broadcastUpdateReq.getScheduledTime());
        broadcast.setTitle(broadcastUpdateReq.getTitle());
        broadcast.setWinnerMessage(broadcastUpdateReq.getWinnerMessage());

        int result = broadcastService.updateBroadcast(broadcast);
        if (result == 0) {
            throw new RuntimeException("broadcast update fail");
        }
        List<Question> questionList = convertQuestionList(broadcastUpdateReq.getQuestionList());
        questionService.updateQuestionListByQuestionId(questionList);

        return HashIdUtils.encryptId(HashIdType.BROADCAST_ID, broadcast.getId());
    }

    public String createBroadcast(BroadcastReq broadcastReq) {
        if (broadcastService.getPreparedBroadcastByUserId(broadcastReq.getUserId())) {
            //TODO: 에러 코드 정의
            throw new RuntimeException("최대 생성 갯수 제한 초과!!");
        }
        // 예약 시간은 현재시간 보다 커야 한다.
        if (!isImmediateStartBroadcast(broadcastReq.getScheduledTime()) && broadcastReq.getScheduledTime() >= System.currentTimeMillis()) {
            //TODO : 에러 코드 정의
            throw new RuntimeException("예약 시간은 현재시간 보다 커야 합니다.");
        }
        // TODO : questionList size가 0보다 커야 함
        if (Objects.isNull(broadcastReq.getQuestionList()) || broadcastReq.getQuestionList().size() == 0) {
            throw new RuntimeException("QuestionList null 또는 size가 0 입니다.");
        }
        // scheduledTime이 null이면 즉시 시작
        Broadcast broadcast = convertBroadcast(broadcastReq);

        broadcastService.insertBroadcast(broadcast);

        log.debug("questionList : {}", broadcastReq);
        log.debug("questionList : {}", broadcastReq.getQuestionList());
        List<Question> questionList = convertQuestionList(broadcastReq.getQuestionList(), broadcast);
        questionService.insertQuestionList(questionList);


        return HashIdUtils.encryptId(HashIdType.BROADCAST_ID, broadcast.getId());
    }

    public void sendAnswer(SendAnswerReq sendAnswerReq) {
        // 해당 방송의 스탭이랑 맞는지 확인
        //validate
        if (!broadcastService.isCurrentBroadcastStep(sendAnswerReq.getBroadcastId(), sendAnswerReq.getStep())) {
            throw new IllegalArgumentException("현재의 broadcast의 step이랑 다릅니다.");
        }
        // 0. 해당 유저가 이전에 정답을 맞춘 유저인지 판단
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(sendAnswerReq.getBroadcastId(),
                                                                           sendAnswerReq.getUserId(),
                                                                           sendAnswerReq.getStep());
        if (PlayUserStatus.PLAY != playUserStatus) {
            // 이전 정답자가 아니기 때문에 필터링
            log.debug("이전 정답자가 아니기 때문에 필터링 되었습니다. userId : {}, broadcastId : {}, step : {}",
                      sendAnswerReq.getUserId(), sendAnswerReq.getBroadcastId(), sendAnswerReq.getStep());
            return;
        }
        // 1. 유저 정답 등록
        broadcastService.insertPlayUserAnswer(sendAnswerReq.getBroadcastId(), sendAnswerReq.getUserId(),
                                              sendAnswerReq.getStep(), sendAnswerReq.getAnswerNo());
        // 2. 유저가 낸 답이 정답인지 확인
        Question question = questionService.getQuestionByBroadcastIdAndStep(sendAnswerReq.getBroadcastId(),
                                                                            sendAnswerReq.getStep());

        if (question.getAnswerNo() == sendAnswerReq.getAnswerNo()) {
            //정답
            //오답시에는 playUser 에 등록되지 않음
            broadcastService.insertPlayUser(sendAnswerReq.getBroadcastId(), sendAnswerReq.getUserId(),
                                            sendAnswerReq.getStep());
        }
        // 3. 퀴즈 통계 등록
        broadcastService.incrementQuestionAnswerStat(sendAnswerReq.getBroadcastId(), sendAnswerReq.getStep(),
                                                     sendAnswerReq.getAnswerNo());

    }

    public StartBroadcastView startBroadcast(StartBroadcastReq startBroadcastReq) {
        // TODO:stream 등록
        // TODO:chat 생성
        // 해당 방송자인지 권한 체크
        checkPermissionBroadcast(startBroadcastReq.getBroadcastId(), startBroadcastReq.getUserId());
        // 방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.WATING, startBroadcastReq.getBroadcastId());

        // TODO:팬들에게 push 발송 현재 전체 발송
        Broadcast broadcast = broadcastService.getBroadcastById(startBroadcastReq.getBroadcastId());
        fcmService.sendStartBroadcastNotice(broadcast);

        return StartBroadcastView.builder()
                                 .broadcastView(buildBroadcastView(broadcast))
                                 .build();
    }

    public void endBroadcast(EndBroadcastReq endBroadcastReq) {
        checkPermissionBroadcast(endBroadcastReq.getBroadcastId(), endBroadcastReq.getUserId());
        // 방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.COMPLETED, endBroadcastReq.getBroadcastId());
        // 방송 종료 push 발송
        fcmService.sendEndBroadcast(endBroadcastReq);
        // stream 업데이트
        // chat close
        // redis에서 해당 방송 관련된 정보 지우기
    }

    public JoinBroadcastView getJoinBroadcastView(long broadcastId, long userId) {
        BroadcastView broadcastView = buildBroadcastView(broadcastService.getBroadcastById(broadcastId),
                                                         userService.getUserById(userId));

        //방송 상태 validate
        // wating, openQuation, openAnswer, openWinners에서만 가능
        if (!broadcastView.getBroadcastStatus().isAccessibleBroadcast()) {
            //TODO : 에러 코드 및 Exception 정하기
            throw new IllegalArgumentException("입장 불가능한 방입니다.");
        }

        JoinBroadcastView joinBroadcastView = new JoinBroadcastView();
        joinBroadcastView.setBroadcastView(broadcastView);

        //viewer 수 증가
        broadcastService.insertViewer(broadcastId, userId);
        //현재 몇 단계 방송 중인지 확인
        Long currentStep = broadcastService.getCurrentBroadcastStep(broadcastId);
        Question question = questionService.getQuestionByBroadcastIdAndStep(broadcastId,
                                                                            currentStep.intValue());
        switch (broadcastView.getBroadcastStatus()) {
            case CREATED:
            case WATING:
                break;
            case OPEN_QUESTION:
                joinBroadcastView.setQuestionProp(question.getQuestionProp());
                joinBroadcastView.setStep(question.getStep());
                break;
            case OPEN_ANSWER:
                joinBroadcastView.setAnswerNo(question.getAnswerNo());
                break;
            case OPEN_WINNER:
            case COMPLETED:
                break;
        }

        // 1번 퀴즈 참가 여부 && 이전 문제 play user이면 play상태
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(broadcastId, userId,
                                                                           currentStep.intValue());
        joinBroadcastView.setPlayUserStatus(playUserStatus);

        //currentViewers
        joinBroadcastView.setViewerCount(broadcastService.getCurrentViewers(broadcastId));

        return joinBroadcastView;
    }

    public void leaveBroadcast(LeaveBroadcastReq leaveBroadcastReq) {
        if (Objects.isNull(userService.getUserById(leaveBroadcastReq.getUserId()))) {
            throw new NotFoundUserException();
        }
        broadcastService.deleteViewer(leaveBroadcastReq.getBroadcastId(), leaveBroadcastReq.getUserId());
    }

    public void updateBroadcastStatus(UpdateBroadcastStatusReq updateBroadcastStatusReq) {
        // TODO : broadcast status 검증 필요
        checkPermissionBroadcast(updateBroadcastStatusReq.getBroadcastId(),
                                 updateBroadcastStatusReq.getUserId());

        broadcastService.updateBroadcastStatus(updateBroadcastStatusReq.getBroadcastStatus(),
                                               updateBroadcastStatusReq.getBroadcastId());
    }

    public void deleteBroadcast(DeleteBroadcastReq deleteBroadcastReq) {
        checkPermissionBroadcast(deleteBroadcastReq.getBroadcastId(), deleteBroadcastReq.getUserId());
        broadcastService.deleteBroadcastById(deleteBroadcastReq.getBroadcastId());
    }

    public QuestionView buildQuestionView(Question question) {
        return QuestionView.builder()
                           .answerNo(question.getAnswerNo())
                           .step(question.getStep())
                           .category(question.getCategory())
                           .questionProp(question.getQuestionProp())
                           .build();
    }

    private BroadcastView buildBroadcastView(Broadcast broadcast) {
        return BroadcastView.builder()
                            .broadcastId(broadcast.getId())
                            .broadcastStatus(broadcast.getBroadcastStatus())
                            .description(broadcast.getDescription())
                            .giftDescription(broadcast.getGiftDescription())
                            .giftType(broadcast.getGiftType())
                            .prize(broadcast.getPrize())
                            .questionCount(broadcast.getQuestionCount())
                            .scheduledTime(broadcast.getScheduledTime())
                            .title(broadcast.getTitle())
                            .winnerMessage(broadcast.getWinnerMessage())
                            .questionCount(broadcast.getQuestionCount())
                            .build();
    }

    private List<BroadcastView> buildBroadcastViewList(List<Broadcast> broadcastList) {
        List<Long> userIds = broadcastList.stream().map(Broadcast::getUserId).collect(Collectors.toList());
        Map<Long, User> userList = userService.getUserByIds(userIds);

        return broadcastList.stream()
                            .map(broadcast -> buildBroadcastView(broadcast,
                                                                 userList.get(broadcast.getUserId()))
                            ).collect(Collectors.toList());
    }

    private BroadcastView buildBroadcastView(Broadcast broadcast, User user) {
        return BroadcastView.builder()
                            .broadcastId(broadcast.getId())
                            .title(broadcast.getTitle())
                            .scheduledTime(broadcast.getScheduledTime())
                            .remainingStartSeconds((broadcast.getScheduledTime() - System.currentTimeMillis())/1000)
                            .prize(broadcast.getPrize())
                            .giftDescription(broadcast.getGiftDescription())
                            .giftType(broadcast.getGiftType())
                            .broadcastStatus(broadcast.getBroadcastStatus())
                            .description(broadcast.getDescription())
                            .questionCount(broadcast.getQuestionCount())
                            .userInfoView(UserInfoView.builder()
                                                      .userId(user.getId())
                                                      .name(user.getName())
                                                      .build())
                            .build();
    }

    private Broadcast convertBroadcast(BroadcastReq broadcastReq) {
        Broadcast broadcast = new Broadcast();
        broadcast.setUserId(broadcastReq.getUserId());
        broadcast.setTitle(broadcastReq.getTitle());
        broadcast.setDescription(broadcastReq.getDescription());
        broadcast.setBroadcastStatus(isImmediateStartBroadcast(broadcastReq.getScheduledTime()) ? BroadcastStatus.WATING : BroadcastStatus.CREATED);
        broadcast.setPrize(broadcastReq.getPrize());
        broadcast.setGiftDescription(broadcastReq.getGiftDescription());
        broadcast.setGiftType(broadcastReq.getGiftType());
        broadcast.setWinnerMessage(broadcastReq.getWinnerMessage());
        broadcast.setQuestionCount(broadcastReq.getQuestionList().size());
        broadcast.setScheduledTime(isImmediateStartBroadcast(broadcastReq.getScheduledTime()) ? System.currentTimeMillis() : broadcastReq.getScheduledTime());

        return broadcast;
    }

    private void checkPermissionBroadcast(long broadcastId, long userId) {
        //TODO: 인터셉터에서 권한 체크가 필요할듯
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);
        if (broadcast.getUserId() != userId) {
            throw new IllegalArgumentException(
                    "해당 유저는 권한이 없습니다. broadcastId : " + broadcastId + " userId : " + userId + "!!");
        }
    }

    private boolean isImmediateStartBroadcast(Long scheduledTime) {
        if (Objects.isNull(scheduledTime)) {
            return true;
        }
        return false;
    }

    private List<Question> convertQuestionList(List<QuestionReq> questionReqList, Broadcast broadcast) {
        List<Question> questionList = Lists.newArrayList();
        for (int i = 0 ; i < questionReqList.size(); ++i) {
            Question question = new Question();
            question.setAnswerNo(questionReqList.get(i).getAnswerNo());
            question.setCategory(questionReqList.get(i).getCategory());
            question.setQuestionProp(questionReqList.get(i).getQuestionProp());
            question.setStep(i+1);
            question.setBroadcastId(broadcast.getId());
            question.setUserId(broadcast.getUserId());

            questionList.add(question);
        }

        return questionList;
    }

    private List<Question> convertQuestionList(List<QuestionView> questionReqList) {
        List<Question> questionList = Lists.newArrayList();
        for (int i = 0 ; i < questionReqList.size(); ++i) {
            Question question = new Question();
            question.setId(questionReqList.get(i).getQuestionId());
            question.setAnswerNo(questionReqList.get(i).getAnswerNo());
            question.setCategory(questionReqList.get(i).getCategory());
            question.setQuestionProp(questionReqList.get(i).getQuestionProp());
            question.setStep(i+1);

            questionList.add(question);
        }

        return questionList;
    }
}
