package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.user.UserDevice;
import me.quiz_together.root.repository.arbitrary.UserDeviceArbitrary;

public class UserDeviceRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private UserDeviceJpaRepository userDeviceJpaRepository;
    private UserDevice userDevice;

    @BeforeEach
    void init() {
        userDevice = UserDeviceArbitrary.defaultOne();
        userDeviceJpaRepository.save(userDevice);
        flushAndClear();
    }

    @Test
    void selectUserDeviceByUserId() {
        UserDevice selectedUserDevice = userDeviceJpaRepository.findByUserId(userDevice.getUserId());
        assertThat(selectedUserDevice).isNotNull();
    }
}