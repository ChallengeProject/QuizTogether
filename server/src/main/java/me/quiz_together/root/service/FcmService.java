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
import me.quiz_together.root.model.firebase.FollowBroadcastMessage;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.firebase.QuestionMessage;
import me.quiz_together.root.model.firebase.WinnersMessage;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.EndBroadcastRequest;
import me.quiz_together.root.model.request.firebase.ChatMessageRequest;
import me.quiz_together.root.model.request.firebase.OpenAnswerRequest;
import me.quiz_together.root.model.request.firebase.OpenQuestionRequest;
import me.quiz_together.root.model.request.firebase.OpenWinnersRequest;
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

    public FcmResponse sendChatMessage(ChatMessageRequest chatMessageRequest, PushType pushType) {
        User user = userService.getUserById(chatMessageRequest.getUserId());

        String to = generateTopics(chatMessageRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        ChatMessage chatMessage = ChatMessage.builder()
                                             .message(chatMessageRequest.getMessage())
                                             .userName(user.getName())
                                             .pushType(pushType)
                                             .build();

        log.debug("chatMessage : {}", chatMessage);
        FcmContainer<ChatMessage> fcmContainer = new FcmContainer<>(to, chatMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendQuestion(OpenQuestionRequest openQuestionRequest) {
        checkPermissionBroadcast(openQuestionRequest.getBroadcastId(), openQuestionRequest.getUserId());
        //broadcast step validation
        // 문제 제출 시에는 step이 올라가있지 않아 -1을 해줌
        validCurrentBroadcastStep(openQuestionRequest.getBroadcastId(), openQuestionRequest.getStep()-1);
        // TODO: 문제 제출 마감시간은 update 이후 n초가 좋아보임
        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openQuestionRequest.getBroadcastId());
        validBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.OPEN_QUESTION, broadcast.getId());
        //문제 제출 마감시간 설정

        //현재의 방송 step 등록
        broadcastService.insertBroadcastStep(openQuestionRequest.getBroadcastId(), openQuestionRequest.getStep());
        Question question = questionService.getQuestionByBroadcastIdAndStep(openQuestionRequest.getBroadcastId(),
                                                                            openQuestionRequest.getStep());

        String to = generateTopics(openQuestionRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        QuestionMessage questionMessage = QuestionMessage.builder()
                                                         .questionProp(question.getQuestionProp())
                                                         .step(openQuestionRequest.getStep())
                                                         .pushType(PushType.QUESTION_MESSAGE)
                                                         .build();

        log.debug("questionMessage : {}", questionMessage);
        FcmContainer<QuestionMessage> fcmContainer = new FcmContainer<>(to, questionMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendAnswer(OpenAnswerRequest openAnswerRequest) {
        checkPermissionBroadcast(openAnswerRequest.getBroadcastId(), openAnswerRequest.getUserId());
        // TODO: 방송 마감 시간 이후 인지 확인하는 로직 추가
        ///// this ///////

        //broadcast step validation
        validCurrentBroadcastStep(openAnswerRequest.getBroadcastId(), openAnswerRequest.getStep());

        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openAnswerRequest.getBroadcastId());
        validBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.OPEN_ANSWER, broadcast.getId());

        Question question = questionService.getQuestionByBroadcastIdAndStep(openAnswerRequest.getBroadcastId(),
                                                                            openAnswerRequest.getStep());

        String to = generateTopics(openAnswerRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        Map<Integer, Integer> questionAnswerStat = broadcastService.getQuestionAnswerStat(
                openAnswerRequest.getBroadcastId(), openAnswerRequest.getStep());

        AnswerMessage answerMessage = AnswerMessage.builder()
                                                   .step(openAnswerRequest.getStep())
                                                   .questionProp(question.getQuestionProp())
                                                   .answerNo(question.getAnswerNo())
                                                   .questionStatistics(questionAnswerStat)
                                                   .pushType(PushType.ANSWER_MESSAGE)
                                                   .build();

        log.debug("answerMessage {}", answerMessage);

        FcmContainer<AnswerMessage> fcmContainer = new FcmContainer<>(to, answerMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendWinners(OpenWinnersRequest openWinnersRequest) {
        checkPermissionBroadcast(openWinnersRequest.getBroadcastId(), openWinnersRequest.getUserId());
        //broadcast step validation
        Broadcast broadcast = broadcastService.getBroadcastById(openWinnersRequest.getBroadcastId());
        int totalStep = broadcast.getQuestionCount();
        validCurrentBroadcastStep(openWinnersRequest.getBroadcastId(), totalStep);
        //방송 상태 validation
        validBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.OPEN_WINNER, broadcast.getId());

        String to = generateTopics(openWinnersRequest.getBroadcastId(), HashIdType.BROADCAST_ID);

        Set<Long> userIds = broadcastService.getPlayUserIds(openWinnersRequest.getBroadcastId(), totalStep);
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

        return fcmResponse;
    }

    public FcmResponse sendEndBroadcast(EndBroadcastRequest endBroadcastRequest) {
        checkPermissionBroadcast(endBroadcastRequest.getBroadcastId(), endBroadcastRequest.getUserId());
        //방송 상태 validation
        //TODO 강제종료 기능 추가시엔 해당 기능 검토 필요
//        Broadcast broadcast = broadcastService.getBroadcastById(endBroadcastRequest.getBroadcastId());
//        validBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.COMPLETED, broadcast.getId());

        String to = generateTopics(endBroadcastRequest.getBroadcastId(), HashIdType.BROADCAST_ID);

        EndBroadcastMessage endBroadcastMessage = EndBroadcastMessage.builder()
                                                                     .pushType(PushType.END_MESSAGE)
                                                                     .build();

        log.debug("endBroadcastMessage {}", endBroadcastMessage);
        FcmContainer<EndBroadcastMessage> fcmContainer = new FcmContainer<>(to, endBroadcastMessage);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendFollowBroadcast(Broadcast broadcast) {
        String to = String.format("%s%s", TO_PREFIX, HashIdUtils.encryptId(HashIdType.USER_ID, broadcast.getUserId()));

        validBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.WATING, broadcast.getId());

        User user = userService.getUserById(broadcast.getUserId());

        FollowBroadcastMessage followBroadcastMessage = FollowBroadcastMessage.builder()
                                                                              .title(broadcast.getTitle())
                                                                              .broadcastId(broadcast.getId())
                                                                              .title(broadcast.getTitle())
                                                                              .description(broadcast.getDescription())
                                                                              .userName(user.getName())
                                                                              .pushType(PushType.FOLLOW_BROADCAST)
                                                                              .build();

        log.debug("FollowBroadcastMessage {}", followBroadcastMessage);
        FcmContainer<FollowBroadcastMessage> fcmContainer = new FcmContainer<>(to, followBroadcastMessage);

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

    private void validCurrentBroadcastStep(long broadcastId, int sendStep) {
        Long currentStep = broadcastService.getCurrentBroadcastStep(broadcastId);
        if (currentStep.intValue() != sendStep) {
            throw new RuntimeException("step 불일치!! currentStep :" + currentStep + " sendStep : " + sendStep);
        }
    }

    private void validBroadcastStatusAndUpdateBroadcastStatus(BroadcastStatus currentBroadcastStatus, BroadcastStatus nextBroadcastStatus, long broadcastId) {
        BroadcastStatus.validateNextBroadcastStatus(currentBroadcastStatus,
                                                    nextBroadcastStatus);
        //방송 상태 변경
        broadcastService.updateBroadcastStatus(nextBroadcastStatus, broadcastId);
    }

    private String generateTopics(Long id, HashIdType hashIdType) {
        return String.format("%s%s", TO_PREFIX, HashIdUtils.encryptId(hashIdType, id));
    }

}
