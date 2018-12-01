package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.model.user.UserReferral;

public class UserReferralArbitrary {
    private Arbitrary<Long> userId = Arbitraries.longs()
                                               .greaterOrEqual(1).unique();
    private Arbitrary<Long> referralUser = Arbitraries.longs()
                                                     .greaterOrEqual(1).unique();
    public static UserReferral defaultOne() {
        return new UserReferralArbitrary().newOne();
    }

    public Arbitrary<UserReferral> arbitrary() {
        return Combinators.combine(
                this.userId, this.referralUser)
                          .as(UserReferral::new);
    }

    public UserReferral newOne() {
        return one(this.arbitrary());
    }
}
