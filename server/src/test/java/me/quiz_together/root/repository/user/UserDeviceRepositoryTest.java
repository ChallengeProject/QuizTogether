package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserDevice;
import me.quiz_together.root.repository.arbitrary.UserDeviceArbitrary;

public class UserDeviceRepositoryTest extends IntegrationTest {

    @Autowired
    private UserDeviceRepository userDeviceRepository;
    private UserDevice userDevice;

    @BeforeEach
    void init() {
        userDevice = UserDeviceArbitrary.defaultOne();
    }

    @Test
    void selectUserDeviceByUserId() {
        userDeviceRepository.insertUserDevice(userDevice);

        UserDevice selectedUserDevice = userDeviceRepository.selectUserDeviceByUserId(userDevice.getUserId());
        assertThat(selectedUserDevice).isNotNull();
    }

    @Test
    void insertUserDevice() {
        userDeviceRepository.insertUserDevice(userDevice);
    }
}