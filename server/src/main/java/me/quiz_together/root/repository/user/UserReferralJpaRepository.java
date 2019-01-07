package me.quiz_together.root.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.user.UserReferral;

public interface UserReferralJpaRepository extends JpaRepository<UserReferral, Long> {
}
