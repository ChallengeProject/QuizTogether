package me.quiz_together.root.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    List<User> findByIdIn(List<Long> ids);
}
