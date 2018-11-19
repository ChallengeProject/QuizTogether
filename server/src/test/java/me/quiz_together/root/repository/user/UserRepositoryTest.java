package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import me.quiz_together.root.model.user.User;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        User user = new User();
        user.setName("woojin");
        user.setMoney(0L);

        userRepository.insertUser(user);
    }

    @After
    public void deleteUser() {
        userRepository.deleteUserById(1);
    }
    @Test
    public void getUserTest() {
        User user = userRepository.selectUserById(1);
        assertThat(user.getName()).isEqualTo("woojin");
    }
}