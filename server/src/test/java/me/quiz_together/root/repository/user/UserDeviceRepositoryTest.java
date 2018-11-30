package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomLong;
import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserDevice;

public class UserDeviceRepositoryTest extends IntegrationTest {

    @Autowired
    private UserDeviceRepository userDeviceRepository;
    private UserDevice userDevice;

    @BeforeEach
    void init() {
        userDevice = new UserDevice();
        userDevice.setPushToken(generateRandomName());
        userDevice.setUserId(generateRandomLong());
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