package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.BroadcastReq;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateReq;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.CurrentBroadcastView;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.user.User;
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

    public List<CurrentBroadcastView> getCurrentBroadcastViewList(long next, int limit) {
        List<Broadcast> broadcastList = broadcastService.getPagingBroadcastList(next, limit);
        List<Long> userIds = broadcastList.stream().map(Broadcast::getUserId).collect(Collectors.toList());

        List<User> userList = userService.getUserByIds(userIds);

        return buildCurrentBroadcastViewList(broadcastList, userList);
    }

    public BroadcastView getBroadcastView(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);

        return BroadcastView.builder()
                            .broadcastId(broadcast.getId())
                            .broadcastStatus(broadcast.getBroadcastStatus())
                            .description(broadcast.getDescription())
                            .giftDescription(broadcast.getGiftDescription())
                            .giftType(broadcast.getGiftType())
                            .prize(broadcast.getPrize())
                            .questionCount(broadcast.getQuestionCount())
                            .scheduledTime(broadcast.getScheduledTime())
                            .title(broadcast.getTitile())
                            .winnerMessage(broadcast.getWinnerMessage())
                            .build();
    }

    public BroadcastForUpdateView getBroadcastForUpdateById(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);
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
        broadcast.setTitile(broadcastUpdateReq.getTitle());
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
        broadcast.setId(broadcastReq.getBroadcastId());
        broadcast.setUserId(broadcastReq.getUserId());
        broadcast.setDescription(broadcastReq.getDescription());
        broadcast.setGiftDescription(broadcastReq.getGiftDescription());
        broadcast.setGiftType(broadcastReq.getGiftType());
        broadcast.setPrize(broadcastReq.getPrize());
        broadcast.setQuestionCount(broadcastReq.getQuestionList().size());
        broadcast.setScheduledTime(broadcastReq.getScheduledTime());
        broadcast.setTitile(broadcastReq.getTitle());
        broadcast.setWinnerMessage(broadcastReq.getWinnerMessage());

        broadcastService.insertBroadcast(broadcast);

        questionService.insertQuestionList(
                broadcastReq.getQuestionList().stream().map(questionReq -> {
                    Question question = new Question();
                    question.setAnswerNo(questionReq.getAnswerNo());
                    question.setCategory(questionReq.getCategory());
                    question.setQuestionProp(questionReq.getQuestionProp());
                    question.setStep(questionReq.getStep());
                    return question;
                }).collect(Collectors.toList()));
    }

    public QuestionView buildQuestionView(Question question) {
        return QuestionView.builder()
                           .answerNo(question.getAnswerNo())
                           .step(question.getStep())
                           .category(question.getCategory())
                           .questionProp(question.getQuestionProp())
                           .build();
    }

    private List<CurrentBroadcastView> buildCurrentBroadcastViewList(List<Broadcast> broadcastList,
                                                                     List<User> userList) {
        List<CurrentBroadcastView> currentBroadcastViewList = Lists.newArrayList();

        int length = broadcastList.size();

        for (int i = 0; i < length; ++i) {
            CurrentBroadcastView currentBroadcastListView = buildCurrentBroadcastView(broadcastList.get(i),
                                                                                      userList.get(i));
            currentBroadcastViewList.add(currentBroadcastListView);
        }

        return currentBroadcastViewList;
    }

    private CurrentBroadcastView buildCurrentBroadcastView(Broadcast broadcast, User user) {
        return CurrentBroadcastView.builder()
                                   .broadcastId(broadcast.getId())
                                   .title(broadcast.getTitile())
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
}
