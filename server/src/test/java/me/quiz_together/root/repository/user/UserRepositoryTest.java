package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import me.quiz_together.root.model.user.User;
import me.quiz_together.root.repository.arbitrary.UserArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;
    private User user;
    UserArbitrary userArbitrary = new UserArbitrary();

    @BeforeEach
    void init() {
        user = UserArbitrary.defaultOne();
    }
    @Test
    void insertTest() {
        userJpaRepository.save(user);
    }

    @Test
    void deleteUser() {
        userJpaRepository.save(user);
        userJpaRepository.deleteById(user.getId());
    }

    @Test
    void selectUserById() {
        userJpaRepository.save(user);
        User woojinUser = userJpaRepository.findById(user.getId()).get();
        assertThat(woojinUser).isNotNull();
    }

    @Test
    void getUserByIds() {

        List<User> users = ArbitraryUtils.list(userArbitrary.arbitrary(), 100);
        users.forEach(u -> userJpaRepository.save(u));
//        Map<Long, User> userMap = userJpaRepository.findUsersByIdIn(users.stream().map(User::getId).collect(Collectors.toList()));
        List<User> empty = userJpaRepository.findByIdIn(Collections.emptyList());
        assertThat(empty).isEmpty();
        List<User> userMap = userJpaRepository.findByIdIn(users.stream().map(User::getId).collect(Collectors.toList()));



        assertThat(userMap).isNotEmpty();
        assertThat(userMap.size()).isGreaterThan(2);
    }

    @Test
    void updateUserProfile() {
        userJpaRepository.save(user);
        String profileImageUrl = generateRandomName();
        user.setProfilePath(profileImageUrl);
        userJpaRepository.save(user);
        User updatedUser = userJpaRepository.findById(user.getId()).get();
        assertThat(updatedUser.getProfilePath()).isEqualTo(profileImageUrl);
    }

    @Test
    void selectUserByName() {
        userJpaRepository.save(user);
        assertThat(userJpaRepository.findByName(user.getName()).getName()).isEqualTo(user.getName());
    }

    @Test
    void login() {
        userJpaRepository.save(user);

        assertThat(userJpaRepository.findById(user.getId())).isNotNull();
    }
}