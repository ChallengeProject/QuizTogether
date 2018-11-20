package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.User;

public class UserRepositoryTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void init() {
        user = new User();
        user.setName(generateRandomName());
        user1 = new User();
        user1.setName(generateRandomName());
        user2 = new User();
        user2.setName(generateRandomName());
        user3 = new User();
        user3.setName(generateRandomName());
    }
    @Test
    public void insertTest() {
        userRepository.insertUser(user);
    }

    @Test
    public void deleteUser() {
        userRepository.insertUser(user);
        userRepository.deleteUserById(1);
    }

    @Test
    public void selectUserById() {
        userRepository.insertUser(user);
        User woojinUser = userRepository.selectUserById(user.getId());
        assertThat(woojinUser).isNotNull();
    }

    @Test
    public void getUserByIds() {
        userRepository.insertUser(user1);
        userRepository.insertUser(user2);
        userRepository.insertUser(user3);

        Map<Long, User> userMap = userRepository.getUserByIds(Lists.newArrayList(user1.getId(), user2.getId(), user3.getId()));

        assertThat(userMap).isNotEmpty();
        assertThat(userMap.size()).isGreaterThan(2);
    }

    @Test
    public void updateUserProfile() {
        userRepository.insertUser(user);
        String profileImageUrl = generateRandomName();
        userRepository.updateUserProfile(user.getId(), profileImageUrl);
        User updatedUser = userRepository.selectUserById(user.getId());
        assertThat(updatedUser.getProfilePath()).isEqualTo(profileImageUrl);
    }

    @Test
    public void selectUserByName() {
        userRepository.insertUser(user);
        assertThat(userRepository.selectUserByName(user.getName()).getName()).isEqualTo(user.getName());
    }

    @Test
    public void login() {
        userRepository.insertUser(user);

        assertThat(userRepository.login(user.getId())).isNotNull();
    }
}