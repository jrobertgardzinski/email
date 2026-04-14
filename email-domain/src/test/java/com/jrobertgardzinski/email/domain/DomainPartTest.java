package com.jrobertgardzinski.email.domain;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Epic("DomainPart")
class DomainPartTest {

    @Property
    @Label("Invariant: rejects invalid input")
    void rejectsInvalidInput(@ForAll("invalidInputs") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThrows(IllegalArgumentException.class, () -> DomainPart.of(tc.get2()));
    }

    @Property
    @Label("Invariant: accepts valid values")
    void acceptsValidValues(@ForAll("validValues") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThatCode(() -> DomainPart.of(tc.get2())).doesNotThrowAnyException();
    }

    @Feature("Domain normalization")
    @DisplayName("normalizes ")
    @ParameterizedTest(name = "\"{0}\" to lowercase \"{1}\"")
    @CsvSource({
            "GMAIL.COM,       gmail.com",
            "Gmail.Com,       gmail.com",
            "googlemail.com,  googlemail.com",
            "HOME.PL,         home.pl",
            "Booking.Co.Uk,   booking.co.uk"
    })
    void normalizesToLowercase(String input, String expected) {
        assertThat(DomainPart.of(input.trim()).value()).isEqualTo(expected.trim());
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> invalidInputs() {
        return Arbitraries.of(
                Tuple.of("null", (String) null),
                Tuple.of("empty", ""),
                Tuple.of("no dot", "localhost")
        );
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> validValues() {
        return Arbitraries.of(
                Tuple.of("simple", "gmail.com"),
                Tuple.of("Polish TLD", "home.pl"),
                Tuple.of("subdomain", "mail.google.com"),
                Tuple.of("compound TLD", "booking.co.uk")
        );
    }
}
