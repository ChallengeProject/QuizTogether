package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.BroadcastReq;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateReq;
import me.quiz_together.root.model.request.broadcast.EndBroadcastReq;
import me.quiz_together.root.model.request.broadcast.LeaveBroadcastReq;
import me.quiz_together.root.model.request.broadcast.SendAnswerReq;
import me.quiz_together.root.model.request.broadcast.StartBroadcastReq;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.CurrentBroadcastView;
import me.quiz_together.root.model.response.broadcast.JoinBroadcastView;
import me.quiz_together.root.model.response.broadcast.StartBroadcastView;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.user.PlayUserStatus;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.service.FcmService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserService;

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

    public List<CurrentBroadcastView> getCurrentBroadcastViewList(long next, int limit) {
        List<Broadcast> broadcastList = broadcastService.getPagingBroadcastList(next, limit);
        List<Long> userIds = broadcastList.stream().map(Broadcast::getUserId).collect(Collectors.toList());

        Map<Long, User> userList = userService.getUserByIds(userIds);

        return buildCurrentBroadcastViewList(broadcastList, userList);
    }

    public BroadcastView getBroadcastView(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);

        return buildBroadcastView(broadcast);

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

    public int updateBroadcast(BroadcastUpdateReq broadcastUpdateReq) {
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
        questionService.updateQuestionListByQuestionId(
                broadcastUpdateReq.getQuestionList().stream().map(questionView -> {
                    Question question = new Question();
                    question.setId(questionView.getQuestionId());
                    question.setAnswerNo(questionView.getAnswerNo());
                    question.setCategory(questionView.getCategory());
                    question.setQuestionProp(questionView.getQuestionProp());
                    question.setStep(questionView.getStep());

                    return question;
                }).collect(Collectors.toList()));
        return result;
    }

    public void createBroadcast(BroadcastReq broadcastReq) {
        Broadcast broadcast = new Broadcast();
        broadcast.setUserId(broadcastReq.getUserId());
        broadcast.setTitle(broadcastReq.getTitle());
        broadcast.setDescription(broadcastReq.getDescription());
        broadcast.setBroadcastStatus(BroadcastStatus.CREATED);
        broadcast.setPrize(broadcastReq.getPrize());
        broadcast.setGiftDescription(broadcastReq.getGiftDescription());
        broadcast.setGiftType(broadcastReq.getGiftType());
        broadcast.setWinnerMessage(broadcastReq.getWinnerMessage());
        broadcast.setQuestionCount(broadcastReq.getQuestionList().size());
        broadcast.setScheduledTime(broadcastReq.getScheduledTime());

        broadcastService.insertBroadcast(broadcast);

        questionService.insertQuestionList(
                broadcastReq.getQuestionList().stream().map(questionReq -> {
                    Question question = new Question();
                    question.setAnswerNo(questionReq.getAnswerNo());
                    question.setCategory(questionReq.getCategory());
                    question.setQuestionProp(questionReq.getQuestionProp());
                    question.setStep(questionReq.getStep());
                    question.setBroadcastId(broadcast.getId());
                    question.setUserId(broadcastReq.getUserId());
                    return question;
                }).collect(Collectors.toList()));
    }

    public void sendAnswer(SendAnswerReq sendAnswerReq) {
        // 해당 방송의 스탭이랑 맞는지 확인
        //validate
        if (!broadcastService.isCurrentBroadcastStep(sendAnswerReq.getBroadcastId(), sendAnswerReq.getStep())) {
            throw new IllegalArgumentException("현재의 broadcast의 step이랑 다릅니다.");
        }
        // 0. 해당 유저가 이전에 정답을 맞춘 유저인지 판단
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(sendAnswerReq.getBroadcastId(),
                                                                       sendAnswerReq.getUserId(), sendAnswerReq.getStep());
        if (PlayUserStatus.PLAY != playUserStatus) {
            // 이전 정답자가 아니기 때문에 필터링
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
        // stream 등록
        // chat 생성
        // 팬들?에게 push 발송
        // 해당 방송자인지 권한 체크
        checkPermissionBroadcast(startBroadcastReq.getBroadcastId(), startBroadcastReq.getUserId());
        // 방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.WATING, startBroadcastReq.getBroadcastId());
        return new StartBroadcastView();
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
        BroadcastView broadcastView = getBroadcastView(broadcastId);

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
        Question question = questionService.getQuestionByBroadcastIdAndStep(broadcastId, currentStep.intValue());
        switch (broadcastView.getBroadcastStatus()) {
            case CREATED:
            case WATING :
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
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(broadcastId, userId, currentStep.intValue());
        joinBroadcastView.setPlayUserStatus(playUserStatus);

        //currentViewers
        joinBroadcastView.setViewerCount(broadcastService.getCurrentViewers(broadcastId));

        return joinBroadcastView;
    }

    public void leaveBroadcast(LeaveBroadcastReq leaveBroadcastReq) {
        broadcastService.deleteViewer(leaveBroadcastReq.getBroadcastId(), leaveBroadcastReq.getUserId());
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
                            .build();
    }

    private List<CurrentBroadcastView> buildCurrentBroadcastViewList(List<Broadcast> broadcastList,
                                                                     Map<Long, User> userList) {
        return broadcastList.stream()
                            .map(broadcast -> buildCurrentBroadcastView(broadcast,
                                                                        userList.get(broadcast.getUserId()))
                            ).collect(Collectors.toList());
    }

    private CurrentBroadcastView buildCurrentBroadcastView(Broadcast broadcast, User user) {
        return CurrentBroadcastView.builder()
                                   .broadcastId(broadcast.getId())
                                   .title(broadcast.getTitle())
                                   .scheduledTime(broadcast.getScheduledTime())
                                   .prize(broadcast.getPrize())
                                   .giftDescription(broadcast.getGiftDescription())
                                   .giftType(broadcast.getGiftType())
                                   .broadcastStatus(broadcast.getBroadcastStatus())
                                   .userView(UserView.builder()
                                                     .userId(user.getId())
                                                     .name(user.getName())
                                                     .build())
                                   .build();
    }

    private void checkPermissionBroadcast(long broadcastId, long userId) {
        //TODO: 인터셉터에서 권한 체크가 필요할듯
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);
        if (broadcast.getUserId() != userId) {
            throw new IllegalArgumentException("해당 유저는 권한이 없습니다.!!");
        }
    }
}
