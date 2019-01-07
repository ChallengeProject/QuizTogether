package me.quiz_together.root.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.user.UserInventory;

public class UserInventoryRepositoryTest extends JpaRepositoryTest {
    public static final int HEART_COUNT = 1;
    @Autowired
    private UserInventoryJpaRepository userInventoryJpaRepository;
    private UserInventory userInventory;

    @BeforeEach
    void init() {
        long userId = AbstractDummy.generateRandomLong();

        userInventory = UserInventory.builder()
                                     .userId(userId)
                                     .heartCount(HEART_COUNT)
                                     .build();
    }

    @Test
    void insertUserInventory() {
        userInventoryJpaRepository.save(userInventory);
        flushAndClear();
    }

    @Test
    void updateUserHeartCount() {
        userInventoryJpaRepository.save(userInventory);
        flushAndClear();

        UserInventory result = userInventoryJpaRepository.findByUserId(userInventory.getUserId());
        assertThat(result).isNotNull();

        int heartCount = AbstractDummy.generateRandomInt();
        userInventory.setHeartCount(heartCount);
        UserInventory updateUserInventory = userInventoryJpaRepository.save(userInventory);
        flushAndClear();
        UserInventory selected = userInventoryJpaRepository.findByUserId(updateUserInventory.getUserId());

        assertThat(selected.getHeartCount()).isEqualTo(heartCount);

    }

    @Test
    void selectUserInventoryByUserId() {
        userInventoryJpaRepository.save(userInventory);
        flushAndClear();
        UserInventory byId = userInventoryJpaRepository.findByUserId(userInventory.getUserId());
        assertThat(byId).isNotNull();
        assertThat(byId.getCreatedTime()).isNotNull();
    }
}