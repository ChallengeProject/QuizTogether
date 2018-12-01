package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.model.user.UserFollower;

public class UserFollowerArbitrary {
    private Arbitrary<Long> userId = Arbitraries.longs()
                                                .greaterOrEqual(1)
                                                .unique();
    private Arbitrary<Long> follower = Arbitraries.longs()
                                                  .greaterOrEqual(1)
                                                  .unique();

    public static UserFollower defaultOne() {
        return new UserFollowerArbitrary().newOne();
    }

    public Arbitrary<UserFollower> arbitrary() {
        return Combinators.combine(
                this.userId, this.follower)
                          .as(UserFollower::new);
    }

    public UserFollower newOne() {
        return one(this.arbitrary());
    }
}
