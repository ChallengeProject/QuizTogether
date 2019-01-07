package me.quiz_together.root.repository.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.user.UserReferral;
import me.quiz_together.root.repository.arbitrary.UserReferralArbitrary;

class UserReferralRepositoryTest extends JpaRepositoryTest {
    @Autowired
    private UserReferralJpaRepository cut;
    private UserReferral userReferral;
    @BeforeEach
    void setUp() {
        userReferral = UserReferralArbitrary.defaultOne();
    }

    @Test
    void insertReferralUser() {
        cut.save(userReferral);
        flushAndClear();
    }

    @Test
    void referral_user_update_fail() {
        UserReferral save1 = cut.save(userReferral);
        flushAndClear();
        UserReferral tempUserReferral = UserReferral.builder()
                                                    .userId(userReferral.getUserId())
                                                    .referralUser(userReferral.getReferralUser() / 2)
                                                    .build();
        assertThat(save1.getReferralUser()).isNotEqualTo(tempUserReferral.getReferralUser());

        UserReferral save = cut.save(tempUserReferral);
        flushAndClear();

        Optional<UserReferral> byId = cut.findById(save.getUserId());

        assertThat(byId.get().getReferralUser()).isNotEqualTo(tempUserReferral.getReferralUser());
    }
}