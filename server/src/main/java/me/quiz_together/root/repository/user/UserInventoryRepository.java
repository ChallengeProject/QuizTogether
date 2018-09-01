package me.quiz_together.root.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserInventoryRepository {
    private UserInventoryMapper userInventoryMapper;

    public void insertUserInventory(Long userId) {
        userInventoryMapper.insertUserInventory(userId);
    }

    public int updateUserHeartCount(Long userId, int heartCount) {
        return userInventoryMapper.updateUserHeartCount(userId, heartCount);
    }
}
