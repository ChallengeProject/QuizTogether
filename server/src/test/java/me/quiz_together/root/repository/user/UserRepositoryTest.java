package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.repository.arbitrary.UserArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

public class UserRepositoryTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    UserArbitrary userArbitrary = new UserArbitrary();

    @BeforeEach
    void init() {
        user = UserArbitrary.defaultOne();
    }
    @Test
    void insertTest() {
        userRepository.insertUser(user);
    }

    @Test
    void deleteUser() {
        userRepository.insertUser(user);
        userRepository.deleteUserById(1);
    }

    @Test
    void selectUserById() {
        userRepository.insertUser(user);
        User woojinUser = userRepository.selectUserById(user.getId());
        assertThat(woojinUser).isNotNull();
    }

    @Test
    void getUserByIds() {

        List<User> users = ArbitraryUtils.list(userArbitrary.arbitrary(), 100);
        users.forEach(u -> userRepository.insertUser(u));
        Map<Long, User> userMap = userRepository.getUserByIds(users.stream().map(User::getId).collect(Collectors.toList()));

        assertThat(userMap).isNotEmpty();
        assertThat(userMap.size()).isGreaterThan(2);
    }

    @Test
    void updateUserProfile() {
        userRepository.insertUser(user);
        String profileImageUrl = generateRandomName();
        userRepository.updateUserProfile(user.getId(), profileImageUrl);
        User updatedUser = userRepository.selectUserById(user.getId());
        assertThat(updatedUser.getProfilePath()).isEqualTo(profileImageUrl);
    }

    @Test
    void selectUserByName() {
        userRepository.insertUser(user);
        assertThat(userRepository.selectUserByName(user.getName()).getName()).isEqualTo(user.getName());
    }

    @Test
    void login() {
        userRepository.insertUser(user);

        assertThat(userRepository.login(user.getId())).isNotNull();
    }
}