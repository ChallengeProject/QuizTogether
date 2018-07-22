package me.quiz_together.root.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.firebase.AnswerMessage;
import me.quiz_together.root.model.firebase.ChatMessage;
import me.quiz_together.root.model.firebase.FcmContainer;
import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.firebase.QuestionMessage;
import me.quiz_together.root.model.firebase.WinnersMessage;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.firebase.ChatMessageReq;
import me.quiz_together.root.model.request.firebase.OpenAnswerReq;
import me.quiz_together.root.model.request.firebase.OpenQuestionReq;
import me.quiz_together.root.model.request.firebase.OpenWinnersReq;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.service.broadcast.BroadcastService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.support.FcmRestTemplate;

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

    public FcmResponse sendChatMessage(ChatMessageReq chatMessageReq) {
        User user = userService.getUserById(chatMessageReq.getUserId());

        String to = TO_PREFIX + chatMessageReq.getBroadcastId();
        ChatMessage chatMessage = ChatMessage.builder()
                                             .message(chatMessageReq.getMessage())
                                             .userName(user.getName())
                                             .build();

        FcmContainer<ChatMessage> fcmContainer = new FcmContainer<>(to, chatMessage, PushType.CHAT_MESSAGE);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendQuestion(OpenQuestionReq openQuestionReq) {
        Broadcast broadcast = broadcastService.getBroadcastById(openQuestionReq.getBroadcastId());
        if (broadcast.getUserId().equals(openQuestionReq.getUserId())) {
            throw new IllegalArgumentException("해당 유저는 권한이 없습니다.!!");
        }
        Question question = questionService.getQuestionByBroadcastIdAndStep(openQuestionReq.getBroadcastId(), openQuestionReq.getStep());

        String to = TO_PREFIX + openQuestionReq.getBroadcastId();
        QuestionMessage questionMessage = QuestionMessage.builder()
                                                         .questionProp(question.getQuestionProp())
                                                         .step(openQuestionReq.getStep())
                                                         .build();

        FcmContainer<QuestionMessage> fcmContainer = new FcmContainer<>(to, questionMessage, PushType.QUESTION_MESSAGE);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendAnswer(OpenAnswerReq openAnswerReq) {
        Broadcast broadcast = broadcastService.getBroadcastById(openAnswerReq.getBroadcastId());
        if (broadcast.getUserId().equals(openAnswerReq.getUserId())) {
            throw new IllegalArgumentException("해당 유저는 권한이 없습니다.!!");
        }
        Question question = questionService.getQuestionByBroadcastIdAndStep(openAnswerReq.getBroadcastId(), openAnswerReq.getStep());

        String to = TO_PREFIX + openAnswerReq.getBroadcastId();
        Map<Integer, Long> questionAnswerStat = broadcastService.getQuestionAnswerStat(openAnswerReq.getBroadcastId(), openAnswerReq.getStep());

        AnswerMessage answerMessage = AnswerMessage.builder()
                                                   .step(openAnswerReq.getStep())
                                                   .questionProp(question.getQuestionProp())
                                                   .answerNo(question.getAnswerNo())
                                                   .questionStatistics(questionAnswerStat)
                                                   .build();
        FcmContainer<AnswerMessage> fcmContainer = new FcmContainer<>(to, answerMessage, PushType.ANSWER_MESSAGE);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

    public FcmResponse sendWinners(OpenWinnersReq openWinnersReq) {
        Broadcast broadcast = broadcastService.getBroadcastById(openWinnersReq.getBroadcastId());
        if (broadcast.getUserId().equals(openWinnersReq.getUserId())) {
            throw new IllegalArgumentException("해당 유저는 권한이 없습니다.!!");
        }

        String to = TO_PREFIX + openWinnersReq.getBroadcastId();

        int lastStep = broadcast.getQuestionCount();
        Set<Long> userIds = broadcastService.getPlayUserIds(openWinnersReq.getBroadcastId(), lastStep);
        Map<Long, User> userList = userService.getUserByIds(userIds);
        List<String> userNameList = userList.values().stream().map(User::getName).collect(Collectors.toList());
        WinnersMessage winnersMessage = WinnersMessage.builder()
                                                         .giftDescription(broadcast.getGiftDescription())
                                                         .giftType(broadcast.getGiftType())
                                                         .prize(broadcast.getPrize())
                                                         .userName(userNameList)
                                                         .winnerMessage(broadcast.getWinnerMessage())
                                                         .build();

        FcmContainer<WinnersMessage> fcmContainer = new FcmContainer<>(to, winnersMessage, PushType.WINNERS_MESSAGE);

        FcmResponse fcmResponse = fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);

        return fcmResponse;
    }

}
