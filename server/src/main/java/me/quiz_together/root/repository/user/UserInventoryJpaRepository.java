package me.quiz_together.root.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.user.UserInventory;

public interface UserInventoryJpaRepository extends JpaRepository<UserInventory, Long> {

    UserInventory findByUserId(Long userId);
}
