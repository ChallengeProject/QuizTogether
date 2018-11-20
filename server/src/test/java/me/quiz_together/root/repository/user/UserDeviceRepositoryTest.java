package me.quiz_together.root.repository.user;

import static me.quiz_together.root.AbstractDummy.generateRandomLong;
import static me.quiz_together.root.AbstractDummy.generateRandomName;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserDevice;

public class UserDeviceRepositoryTest extends IntegrationTest {

    @Autowired
    private UserDeviceRepository userDeviceRepository;
    private UserDevice userDevice;

    @Test
    public void selectUserDeviceByUserId() {
        userDeviceRepository.insertUserDevice(userDevice);

        UserDevice selectedUserDevice = userDeviceRepository.selectUserDeviceByUserId(userDevice.getUserId());
        assertThat(selectedUserDevice).isNotNull();
    }

    @Test
    public void insertUserDevice() {
        userDeviceRepository.insertUserDevice(userDevice);
    }

    @Before
    public void init() {
        userDevice = new UserDevice();
        userDevice.setPushToken(generateRandomName());
        userDevice.setUserId(generateRandomLong());
    }
}