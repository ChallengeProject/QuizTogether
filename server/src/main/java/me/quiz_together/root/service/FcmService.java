package me.quiz_together.root.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.firebase.AnswerMessage;
import me.quiz_together.root.model.firebase.ChatMessage;
import me.quiz_together.root.model.firebase.EndBroadcastMessage;
import me.quiz_together.root.model.firebase.FcmContainer;
import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.firebase.NoticeMessage;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.firebase.QuestionMessage;
import me.quiz_together.root.model.firebase.WinnersMessage;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.EndBroadcastReq;
import me.quiz_together.root.model.request.firebase.ChatMessageReq;
import me.quiz_together.root.model.request.firebase.OpenAnswerReq;
import me.quiz_together.root.model.request.firebase.OpenQuestionReq;
import me.quiz_together.root.model.request.firebase.OpenWinnersReq;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.service.broadcast.BroadcastService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.support.FcmRestTemplate;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

@Slf4j
@Service
public class FcmService {
    private static final String TO_PREFIX = "/topics/";
    @Autowired
    private UserService userService;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FcmRestTemplate fcmRestTemplate;

    public FcmResponse sendChatMessage(ChatMessageReq chatMessageReq, PushType pushType) {
        User user = userService.getUserById(chatMessageReq.getUserId());

        String to = generateTopics(chatMessageReq.getBroadcastId(), HashIdType.BROADCAST_ID);
        ChatMessage chatMessage = ChatMessage.builder()
                                             .message(chatMessageReq.getMessage())
                                             .userName(user.getName())
                                             .pushType(pushType)
                                             .build();

        log.debug("chatMessage : {}", chatMessage);
        FcmContainer<ChatMessage> fcmContainer = new FcmContainer<>(to, chatMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendQuestion(OpenQuestionReq openQuestionReq) {

        checkPermissionBroadcast(openQuestionReq.getBroadcastId(), openQuestionReq.getUserId());
        //현재의 방송 step 등록
        broadcastService.insertBroadcastStep(openQuestionReq.getBroadcastId(), openQuestionReq.getStep());
        Question question = questionService.getQuestionByBroadcastIdAndStep(openQuestionReq.getBroadcastId(),
                                                                            openQuestionReq.getStep());

        String to = generateTopics(openQuestionReq.getBroadcastId(), HashIdType.BROADCAST_ID);
        QuestionMessage questionMessage = QuestionMessage.builder()
                                                         .questionProp(question.getQuestionProp())
                                                         .step(openQuestionReq.getStep())
                                                         .pushType(PushType.QUESTION_MESSAGE)
                                                         .build();

        log.debug("questionMessage : {}", questionMessage);
        FcmContainer<QuestionMessage> fcmContainer = new FcmContainer<>(to, questionMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        // TODO: 문제 제출 마감시간은 update 이후 n초가 좋아보임
        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openQuestionReq.getBroadcastId());
        BroadcastStatus.validateNextBroadcastStatus(broadcast.getBroadcastStatus(),
                                                    BroadcastStatus.OPEN_QUESTION);
        //방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.OPEN_QUESTION, openQuestionReq.getBroadcastId());
        //문제 제출 마감시간 설정

        return fcmResponse;
    }

    public FcmResponse sendAnswer(OpenAnswerReq openAnswerReq) {
        checkPermissionBroadcast(openAnswerReq.getBroadcastId(), openAnswerReq.getUserId());
        // TODO: 방송 마감 시간 이후 인지 확인하는 로직 추가
        ///// this ///////

        Question question = questionService.getQuestionByBroadcastIdAndStep(openAnswerReq.getBroadcastId(),
                                                                            openAnswerReq.getStep());

        String to = generateTopics(openAnswerReq.getBroadcastId(), HashIdType.BROADCAST_ID);
        Map<Integer, Integer> questionAnswerStat = broadcastService.getQuestionAnswerStat(
                openAnswerReq.getBroadcastId(), openAnswerReq.getStep());

        AnswerMessage answerMessage = AnswerMessage.builder()
                                                   .step(openAnswerReq.getStep())
                                                   .questionProp(question.getQuestionProp())
                                                   .answerNo(question.getAnswerNo())
                                                   .questionStatistics(questionAnswerStat)
                                                   .pushType(PushType.ANSWER_MESSAGE)
                                                   .build();

        log.debug("answerMessage {}", answerMessage);

        FcmContainer<AnswerMessage> fcmContainer = new FcmContainer<>(to, answerMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openAnswerReq.getBroadcastId());
        BroadcastStatus.validateNextBroadcastStatus(broadcast.getBroadcastStatus(),
                                                    BroadcastStatus.OPEN_ANSWER);
        //방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.OPEN_ANSWER, openAnswerReq.getBroadcastId());

        return fcmResponse;
    }

    public FcmResponse sendWinners(OpenWinnersReq openWinnersReq) {
        checkPermissionBroadcast(openWinnersReq.getBroadcastId(), openWinnersReq.getUserId());
        Broadcast broadcast = broadcastService.getBroadcastById(openWinnersReq.getBroadcastId());
        String to = generateTopics(openWinnersReq.getBroadcastId(), HashIdType.BROADCAST_ID);

        int lastStep = broadcast.getQuestionCount();
        Set<Long> userIds = broadcastService.getPlayUserIds(openWinnersReq.getBroadcastId(), lastStep);
        Map<Long, User> userList = userService.getUserByIds(userIds);
        List<String> userNameList = userList.values().stream().map(User::getName).collect(Collectors.toList());

        //TODO : 상금이 PRize인 경우 우승 자 수 만큼 나눈 뒤에 소수 자리 정하기

        WinnersMessage winnersMessage = WinnersMessage.builder()
                                                      .giftDescription(broadcast.getGiftDescription())
                                                      .giftType(broadcast.getGiftType())
                                                      .prize(broadcast.getPrize())
                                                      .userName(userNameList)
                                                      .winnerMessage(broadcast.getWinnerMessage())
                                                      .pushType(PushType.WINNERS_MESSAGE)
                                                      .build();

        log.debug("winnersMessage : {}", winnersMessage);
        FcmContainer<WinnersMessage> fcmContainer = new FcmContainer<>(to, winnersMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        //방송 상태 validation
        BroadcastStatus.validateNextBroadcastStatus(broadcast.getBroadcastStatus(),
                                                    BroadcastStatus.OPEN_WINNER);
        //방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.OPEN_WINNER, openWinnersReq.getBroadcastId());

        return fcmResponse;
    }

    public FcmResponse sendEndBroadcast(EndBroadcastReq endBroadcastReq) {
        checkPermissionBroadcast(endBroadcastReq.getBroadcastId(), endBroadcastReq.getUserId());
        String to = generateTopics(endBroadcastReq.getBroadcastId(), HashIdType.BROADCAST_ID);

        // TODO: 방송 종료시에 무조건 위너 상태여야 하는지?
        EndBroadcastMessage endBroadcastMessage = EndBroadcastMessage.builder()
                                                                     .pushType(PushType.END_MESSAGE)
                                                                     .build();

        log.debug("endBroadcastMessage {}", endBroadcastMessage);
        FcmContainer<EndBroadcastMessage> fcmContainer = new FcmContainer<>(to, endBroadcastMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendStartBroadcastNotice(Broadcast broadcast) {
        String to = generateTopics(broadcast.getId(), HashIdType.BROADCAST_ID);

        NoticeMessage noticeMessage = NoticeMessage.builder()
                                                   .title(broadcast.getTitle())
                                                   .broadcastId(broadcast.getId())
                                                   .userId(broadcast.getUserId())
                                                   .pushType(PushType.NOTICE_MESSAGE)
                                                   .build();

        log.debug("NoticeMessage {}", noticeMessage);
        FcmContainer<NoticeMessage> fcmContainer = new FcmContainer<>(to, noticeMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);
        return fcmResponse;
    }

    private void checkPermissionBroadcast(long broadcastId, long userId) {
        //TODO: 인터셉터에서 권한 체크가 필요할듯
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);
        if (broadcast.getUserId() != userId) {
            throw new IllegalArgumentException(
                    "해당 유저는 권한이 없습니다. broadcastId : " + broadcastId + " userId : " + userId + "!!");
        }
    }

    private String generateTopics(Long id, HashIdType hashIdType) {
        return String.format("%s%s", TO_PREFIX, HashIdUtils.encryptId(hashIdType, id));
    }

}
