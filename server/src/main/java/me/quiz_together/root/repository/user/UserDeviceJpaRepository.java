package me.quiz_together.root.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.user.UserDevice;

public interface UserDeviceJpaRepository extends JpaRepository<UserDevice, Long> {

    UserDevice findByUserId(Long userId);
}
