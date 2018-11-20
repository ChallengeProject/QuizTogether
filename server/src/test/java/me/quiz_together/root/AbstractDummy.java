package me.quiz_together.root;

import java.util.Arrays;
import java.util.Random;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class AbstractDummy {
    protected static final int SMALLINT = 255;
    protected static final DateTime STARTDATE = DateTime.now().minusYears(1).withMillisOfSecond(0);
    protected static final DateTime ENDDATE = DateTime.now().plusYears(1).withMillisOfSecond(0);

    public static String generateRandomName() {
        return generateRandomName(3, 10);
    }

    public static String generateRandomName(int length) {
        return generateRandomName(1, length);
    }

    public static String generateRandomName(int minLength, int maxLength) {
        return RandomStringUtils.random(RandomUtils.nextInt(minLength, maxLength), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ");
    }

    public static String generateRandomId() {
        return generateRandomId(3, 10);
    }

    public static String generateRandomId(int length) {
        return generateRandomId(length, length);
    }

    public static String generateRandomId(int minLength, int maxLength) {
        return RandomStringUtils.random(RandomUtils.nextInt(minLength, maxLength), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    public static boolean generateRandomBoolean() {
        return BooleanUtils.toBoolean(RandomUtils.nextInt(0, 2));
    }

    public static int generateRandomInt(int maxValue) {
        return RandomUtils.nextInt(1, maxValue);
    }

    public static float generateRandomFloat(float maxValue) {
        return RandomUtils.nextFloat(1, maxValue);
    }

    public static int generateRandomInt() {
        return RandomUtils.nextInt(0, Integer.MAX_VALUE);
    }

    public static long generateRandomLong() {
        return RandomUtils.nextLong(0, Long.MAX_VALUE);
    }

    public static DateTime now() {
        return DateTime.now().withMillisOfSecond(0);
    }

    public static int getCurrentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String generateRandomIp() {
        return StringUtils.join(
                Arrays.asList(generateRandomInt(255), generateRandomInt(255), generateRandomInt(255), generateRandomInt(255)), '.');
    }

    public static String generateRandomHex(int length) {
        return RandomStringUtils.random(length, "0123456789abcdef");
    }

    @NotNull
    public static <EnumClassType extends Enum<EnumClassType>> EnumClassType getRandomValueType(@NotNull Class<EnumClassType> enumClass) {
        return enumClass.getEnumConstants()[new Random().nextInt(enumClass.getEnumConstants().length)];
    }
}
