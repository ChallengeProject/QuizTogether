package me.quiz_together.root.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.user.UserFollower;

public interface UserFollowerJpaRepository extends JpaRepository<UserFollower, Long> {

    List<UserFollower> findUserFollowersByUserId(Long userId);
}
