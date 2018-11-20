package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserInventory;

public class UserInventoryRepositoryTest extends IntegrationTest {

    @Autowired
    private UserInventoryRepository userInventoryRepository;

    @Test
    public void insertUserInventory() {
        userInventoryRepository.insertUserInventory(AbstractDummy.generateRandomLong());
    }

    @Test
    public void updateUserHeartCount() {
        long userId = AbstractDummy.generateRandomLong();

        userInventoryRepository.insertUserInventory(userId);

        UserInventory userInventory = userInventoryRepository.selectUserInventoryByUserId(userId);
        assertThat(userInventory).isNotNull();

        int heartCount = AbstractDummy.generateRandomInt();
        userInventoryRepository.updateUserHeartCount(userId, heartCount);
        UserInventory selected = userInventoryRepository.selectUserInventoryByUserId(userId);

        assertThat(selected.getHeartCount()).isEqualTo(heartCount + 1);

    }

    @Test
    public void selectUserInventoryByUserId() {
        long userId = AbstractDummy.generateRandomLong();

        userInventoryRepository.insertUserInventory(userId);
        UserInventory userInventory = userInventoryRepository.selectUserInventoryByUserId(
                userId);
        assertThat(userInventory).isNotNull();
    }
}