package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.response.broadcast.CurrentBroadcastListView;
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

    public List<CurrentBroadcastListView> getCurrentBroadcastListView(long next, int limit) {
        List<Broadcast> broadcastList = broadcastService.getPagingBroadcastList(next, limit);
        List<Long> userIds = broadcastList.stream().map(Broadcast::getUserId).collect(Collectors.toList());

        List<User> userList = userService.getUserByIds(userIds);

        return buildCurrentBroadcastListViewList(broadcastList, userList);
    }

    private List<CurrentBroadcastListView> buildCurrentBroadcastListViewList(List<Broadcast> broadcastList, List<User> userList) {
        List<CurrentBroadcastListView> currentBroadcastListViewList = Lists.newArrayList();

        int length = broadcastList.size();

        for(int i = 0 ; i < length ; ++i) {
            CurrentBroadcastListView currentBroadcastListView = buildCurrentBroadcastListView(broadcastList.get(i), userList.get(i));
            currentBroadcastListViewList.add(currentBroadcastListView);
        }

        return currentBroadcastListViewList;
    }
    private CurrentBroadcastListView buildCurrentBroadcastListView(Broadcast broadcast, User user) {
        return CurrentBroadcastListView.builder()
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
