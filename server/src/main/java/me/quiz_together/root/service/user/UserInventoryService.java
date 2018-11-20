package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import me.quiz_together.root.exceptions.AbusingUserException;
import me.quiz_together.root.model.user.UserInventory;
import me.quiz_together.root.repository.user.UserInventoryRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserInventoryService {
    private static final int HEART_COUNT_MAX = 5;
    private UserInventoryRepository userInventoryRepository;

    public UserInventory getUserInventoryByUserId(Long userId) {
        return userInventoryRepository.selectUserInventoryByUserId(userId);
    }

    public void insertUserInventory(Long userId) {
        userInventoryRepository.insertUserInventory(userId);
    }

    public int updateUserHeartCount(Long userId, int heartCount) {
        //TODO : plus, minus 따로 구현하기
        if (heartCount > HEART_COUNT_MAX) {
            throw new AbusingUserException("heart count max 초과");
        }
        return userInventoryRepository.updateUserHeartCount(userId, heartCount);
    }
}
