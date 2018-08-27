package me.quiz_together.root.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.request.user.UserFollowerRequest;
import me.quiz_together.root.model.response.user.UserFollowerView;
import me.quiz_together.root.model.user.UserFollower;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserFollowerViewService {
    private UserFollowerService userFollowerService;

    public void insertFollower(UserFollowerRequest userFollowerRequest) {
        if (userFollowerRequest.getUserId().equals(userFollowerRequest.getFollower())) {
            // TODO : error code 정의
            throw new RuntimeException("userId : " + userFollowerRequest.getUserId() + " follower : " + userFollowerRequest.getFollower());
        }
        userFollowerService.insertFollower(convertUserFollower(userFollowerRequest));
    }

    public void deleteFollower(UserFollowerRequest userFollowerRequest) {
        userFollowerService.deleteFollower(convertUserFollower(userFollowerRequest));
    }

    public List<UserFollowerView> getFollowerListByUserId(Long userId) {
        List<UserFollower> userFollowerList = userFollowerService.getFollowerListByUserId(userId);

        return userFollowerList.stream().map(UserFollowerView::new).collect(Collectors.toList());
    }

    private UserFollower convertUserFollower(UserFollowerRequest userFollowerRequest) {
        UserFollower userFollower = new UserFollower();
        userFollower.setFollower(userFollowerRequest.getFollower());
        userFollower.setUserId(userFollowerRequest.getUserId());

        return userFollower;
    }
}
