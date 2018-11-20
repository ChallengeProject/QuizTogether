package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserFollower;

public class UserFollowerRepositoryTest extends IntegrationTest {

    @Autowired
    private UserFollowerRepository userFollowerRepository;
    private UserFollower userFollower;

    @Before
    public void init() {
        userFollower = new UserFollower();
        userFollower.setFollower(AbstractDummy.generateRandomLong());
        userFollower.setUserId(AbstractDummy.generateRandomLong());
    }

    @Test
    public void insertFollower() {
        userFollowerRepository.insertFollower(userFollower);
    }

    @Test
    public void deleteFollower() {
        userFollowerRepository.insertFollower(userFollower);
        userFollowerRepository.deleteFollower(userFollower);
    }

    @Test
    public void selectFollowerListByUserId() {
        userFollowerRepository.insertFollower(userFollower);
        List<UserFollower> userFollowerList = userFollowerRepository.selectFollowerListByUserId(userFollower.getUserId());
        assertThat(userFollowerList).isNotEmpty();

    }
}