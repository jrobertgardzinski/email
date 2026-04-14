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

@Epic("LocalPart")
class LocalPartTest {

    @Property
    @Label("Invariant: rejects invalid input")
    void rejectsInvalidInput(@ForAll("invalidInputs") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThrows(IllegalArgumentException.class, () -> LocalPart.of(tc.get2()));
    }

    @Property
    @Label("Invariant: accepts valid values")
    void acceptsValidValues(@ForAll("validValues") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThatCode(() -> LocalPart.of(tc.get2())).doesNotThrowAnyException();
    }

    @Feature("Gmail normalization")
    @DisplayName("normalizes ")
    @ParameterizedTest(name = "\"{0}\" at gmail.com to \"{1}\"")
    @CsvSource({
            "J.Doe+spam, jdoe",
            "j.d.o.e,    jdoe",
            "USER+work,  user",
            "plain,      plain"
    })
    void normalizesGmail(String local, String expected) {
        assertThat(LocalPart.of(local.trim()).normalize("gmail.com").value()).isEqualTo(expected.trim());
    }

    @Feature("Yahoo normalization")
    @DisplayName("normalizes ")
    @ParameterizedTest(name = "\"{0}\" at yahoo.com to \"{1}\"")
    @CsvSource({
            "J.Doe-lists, j.doe",
            "USER,        user"
    })
    void normalizesYahoo(String local, String expected) {
        assertThat(LocalPart.of(local.trim()).normalize("yahoo.com").value()).isEqualTo(expected.trim());
    }

    @Feature("Outlook normalization")
    @DisplayName("normalizes ")
    @ParameterizedTest(name = "\"{0}\" at outlook.com to \"{1}\"")
    @CsvSource({
            "J.Doe+alias, j.doe",
            "J.Doe,       j.doe"
    })
    void normalizesOutlook(String local, String expected) {
        assertThat(LocalPart.of(local.trim()).normalize("outlook.com").value()).isEqualTo(expected.trim());
    }

    @Feature("iCloud normalization")
    @DisplayName("normalizes ")
    @ParameterizedTest(name = "\"{0}\" at icloud.com to \"{1}\"")
    @CsvSource({
            "J.Doe, j.doe",
            "USER,  user"
    })
    void normalizesICloud(String local, String expected) {
        assertThat(LocalPart.of(local.trim()).normalize("icloud.com").value()).isEqualTo(expected.trim());
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> invalidInputs() {
        return Arbitraries.of(
                Tuple.of("null", (String) null),
                Tuple.of("empty", ""),
                Tuple.of("leading dot", ".john"),
                Tuple.of("trailing dot", "john.")
        );
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> validValues() {
        return Arbitraries.of(
                Tuple.of("plain", "user"),
                Tuple.of("with dot", "j.doe"),
                Tuple.of("with alias", "j.doe+spam"),
                Tuple.of("alphanumeric", "user123")
        );
    }
}
