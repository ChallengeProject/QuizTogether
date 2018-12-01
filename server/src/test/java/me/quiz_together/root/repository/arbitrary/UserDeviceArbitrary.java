package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.model.user.UserDevice;

public class UserDeviceArbitrary {
    private Arbitrary<Long> userId = Arbitraries.longs()
                                                .greaterOrEqual(1).unique();
    private Arbitrary<String> pushToken = Arbitraries.strings()
                                                     .all()
                                                     .ofMinLength(1)
                                                     .ofMaxLength(300)
                                                     .unique();
    private Arbitrary<Long> createdTime = Arbitraries.longs()
                                                     .greaterOrEqual(0);
    private Arbitrary<Long> updatedTime = Arbitraries.longs()
                                                     .greaterOrEqual(0);

    public static UserDevice defaultOne() {
        return new UserDeviceArbitrary().newOne();
    }

    public Arbitrary<UserDevice> arbitrary() {
        return Combinators.combine(
                this.userId, this.pushToken, this.createdTime, this.updatedTime)
                          .as((userId, pushToken, createdTime, updatedTime) ->
                                      UserDevice.builder()
                                                .userId(userId)
                                                .pushToken(pushToken)
                                                .createdTime(createdTime)
                                                .updatedTime(updatedTime)
                                                .build());
    }

    public UserDevice newOne() {
        return one(this.arbitrary());
    }
}
