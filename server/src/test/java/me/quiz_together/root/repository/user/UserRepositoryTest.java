package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.repository.arbitrary.UserArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

public class UserRepositoryTest extends JpaRepositoryTest {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private User user;
    UserArbitrary userArbitrary = new UserArbitrary();

    @BeforeEach
    void init() {
        user = UserArbitrary.defaultOne();
    }

    @Test
    void deleteUser() {
        userJpaRepository.save(user);
        flushAndClear();
        User actual = userJpaRepository.findById(user.getId()).get();
        userJpaRepository.deleteById(actual.getId());

        User result = userJpaRepository.findById(this.user.getId()).orElse(null);
        assertThat(result).isNull();
    }

    @Test
    void selectUserById() {
        userJpaRepository.save(user);
        flushAndClear();
        User woojinUser = userJpaRepository.findById(user.getId()).get();
        assertThat(woojinUser).isNotNull();
    }

    @Test
    void getUserByIds() {
        List<User> users = ArbitraryUtils.list(userArbitrary.arbitrary(), 100);
        users.forEach(u -> userJpaRepository.save(u));
//        Map<Long, User> userList = userJpaRepository.findUsersByIdIn(users.stream().map(User::getId).collect(Collectors.toList()));
        flushAndClear();
        List<User> empty = userJpaRepository.findByIdIn(Collections.emptyList());
        assertThat(empty).isEmpty();
        List<User> userList = userJpaRepository.findByIdIn(
                users.stream().map(User::getId).collect(Collectors.toList()));

        assertThat(userList).isNotEmpty();
        assertThat(userList).hasSize(users.size());
    }

    @Test
    void updateUserProfile() {
        userJpaRepository.save(user);
        flushAndClear();
        String profileImageUrl = generateRandomName();
        user.setProfilePath(profileImageUrl);
        userJpaRepository.save(user);
        flushAndClear();
        User updatedUser = userJpaRepository.findById(user.getId()).get();
        assertThat(updatedUser.getProfilePath()).isEqualTo(profileImageUrl);
    }

    @Test
    void selectUserByName() {
        userJpaRepository.save(user);
        flushAndClear();
        assertThat(userJpaRepository.findByName(user.getName()).getName()).isEqualTo(user.getName());
    }

    @Test
    void login() {
        userJpaRepository.save(user);
        flushAndClear();
        assertThat(userJpaRepository.findById(user.getId())).isNotNull();
    }
}