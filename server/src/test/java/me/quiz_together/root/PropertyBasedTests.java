package me.quiz_together.root;

import static org.assertj.core.api.Assertions.assertThat;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;

public class PropertyBasedTests {

    @Property
    boolean absoluteValueOAllNumbersIsPositive(@ForAll @IntRange(max = Integer.MAX_VALUE) int anInteger) {
        return Math.abs(anInteger) >= 0;
    }

    @Property
    void lengthOfConcatenatedStringIsGreaterThenLengthOfEach(@ForAll @StringLength(min = 1) String string1, @ForAll @StringLength(min = 1) String string2) {
        String conc = string1 + string2;
        assertThat(conc.length()).isGreaterThan(string1.length());
        assertThat(conc.length()).isGreaterThan(string2.length());
    }
}
