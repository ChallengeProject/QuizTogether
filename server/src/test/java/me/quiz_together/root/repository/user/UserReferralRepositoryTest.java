package me.quiz_together.root.repository.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.user.UserReferral;
import me.quiz_together.root.repository.arbitrary.UserReferralArbitrary;

class UserReferralRepositoryTest extends IntegrationTest {
    @Autowired
    private UserReferralRepository cut;
    private UserReferral userReferral;
    @BeforeEach
    void setUp() {
        userReferral = UserReferralArbitrary.defaultOne();
    }

    @Test
    void insertReferralUser() {
        cut.insertReferralUser(userReferral);
    }
}