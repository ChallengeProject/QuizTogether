package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserStatus;

public class UserArbitrary {
    private Arbitrary<String> name = Arbitraries.strings()
                                                .all()
                                                .ofMinLength(1)
                                                .ofMaxLength(40)
                                                .unique();
    private Arbitrary<String> profilePath = Arbitraries.strings()
                                                       .alpha()
                                                       .ofMinLength(1)
                                                       .ofMaxLength(300)
                                                       .unique();
    private Arbitrary<Long> money = Arbitraries.longs()
                                               .greaterOrEqual(0);
    private Arbitrary<Long> createdTime = Arbitraries.longs()
                                                     .greaterOrEqual(0);
    private Arbitrary<Long> updatedTime = Arbitraries.longs()
                                                     .greaterOrEqual(0);
    private Arbitrary<Long> deletedTime = Arbitraries.longs()
                                                     .greaterOrEqual(0);

    private Arbitrary<UserStatus> userStatus = Arbitraries.constant(
            AbstractDummy.getRandomValueType(UserStatus.class));

    public static User defaultOne() {
        return new UserArbitrary().newOne();
    }

    public Arbitrary<User> arbitrary() {
        return Combinators.combine(
                this.name, this.profilePath, this.money, this.createdTime, this.updatedTime,
                this.deletedTime, this.userStatus)
                          .as((name, profilePath, money, createdTime, updatedTime, deletedTime, userStatus) ->
                                      User.builder()
                                          .name(name)
                                          .profilePath(profilePath)
                                          .money(money)
                                          .createdTime(createdTime)
                                          .updatedTime(updatedTime)
                                          .deletedTime(deletedTime)
                                          .userStatus(userStatus)
                                          .build());
    }

    public User newOne() {
        return one(this.arbitrary());
    }
}
