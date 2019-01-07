package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.repository.arbitrary.UserFollowerArbitrary;

public class UserFollowerRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private UserFollowerJpaRepository cut;
    private UserFollower userFollower;

    @BeforeEach
    void init() {
        userFollower = UserFollowerArbitrary.defaultOne();
    }

    @Test
    void insertFollower() {
        cut.save(userFollower);
        flushAndClear();
        List<UserFollower> byUserId = cut.findUserFollowersByUserId(userFollower.getUserId());

        assertThat(byUserId).isNotEmpty();
    }

    @Test
    void deleteFollower() {
        UserFollower defaultOne = UserFollowerArbitrary.defaultOne();
        cut.save(defaultOne);
        flushAndClear();
        cut.delete(defaultOne);

        List<UserFollower> byUserId = cut.findUserFollowersByUserId(defaultOne.getUserId());

        assertThat(byUserId).isEmpty();

    }

    @Test
    void selectFollowerListByUserId() {
        cut.save(userFollower);
        flushAndClear();
        List<UserFollower> userFollowerList = cut.findUserFollowersByUserId(userFollower.getUserId());
        assertThat(userFollowerList).isNotEmpty();

    }
}