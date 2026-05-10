package com.jrobertgardzinski.email.domain;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Epic("Email")
@Feature("Domain")
@Story("LocalPart")
class LocalPartTest {

    @Property
    @Label("Invariant: rejects invalid input")
    void rejectsInvalidInput(@ForAll("invalidInputs") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThrows(IllegalArgumentException.class, () -> LocalPart.of(tc.get2()));
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> invalidInputs() {
        return Arbitraries.of(
                Tuple.of("leading dot", ".john"),
                Tuple.of("trailing dot", "john.")
        );
    }

    @Property
    @Label("Invariant: accepts valid values")
    void acceptsValidValues(@ForAll("validValues") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThatCode(() -> LocalPart.of(tc.get2())).doesNotThrowAnyException();
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

    record Normalization(String entered, String expected) {}

    @Label("Gmail normalization")
    @Property
    void normalizesGmail(@ForAll("gmailNormalization") Normalization normalization) {
        Allure.parameter("normalizes", String.format("%s -> %s", normalization.entered, normalization.expected));
        assertThat(LocalPart.of(normalization.entered).normalize("gmail.com").value()).isEqualTo(normalization.expected);
    }
    @Provide
    Arbitrary<Normalization> gmailNormalization() {
        return Arbitraries.of(
                new Normalization("J.Doe+spam", "jdoe"),
                new Normalization("j.d.o.e", "jdoe"),
                new Normalization("JDOE+work", "jdoe")
        );
    }

    @Label("Yahoo normalization")
    @Property
    void normalizesYahoo(@ForAll("yahooNormalization") Normalization normalization) {
        Allure.parameter("normalizes", String.format("%s -> %s", normalization.entered, normalization.expected));
        assertThat(LocalPart.of(normalization.entered).normalize("yahoo.com").value()).isEqualTo(normalization.expected);
    }
    @Provide
    Arbitrary<Normalization> yahooNormalization() {
        return Arbitraries.of(
                new Normalization("J.Doe-lists", "j.doe"),
                new Normalization("USER", "user")
        );
    }

    @Label("Outlook normalization")
    @Property
    void normalizesOutlook(@ForAll("outlookNormalization") Normalization normalization) {
        Allure.parameter("normalizes", String.format("%s -> %s", normalization.entered, normalization.expected));
        assertThat(LocalPart.of(normalization.entered).normalize("outlook.com").value()).isEqualTo(normalization.expected);
    }
    @Provide
    Arbitrary<Normalization> outlookNormalization() {
        return Arbitraries.of(
                new Normalization("J.Doe+alias", "j.doe"),
                new Normalization("J.Doe", "j.doe")
        );
    }

    @Label("iCloud normalization")
    @Property
    void normalizesICloud(@ForAll("iCloudNormalization") Normalization normalization) {
        Allure.parameter("normalizes", String.format("%s -> %s", normalization.entered, normalization.expected));
        assertThat(LocalPart.of(normalization.entered).normalize("icloud.com").value()).isEqualTo(normalization.expected);
    }
    @Provide
    Arbitrary<Normalization> iCloudNormalization() {
        return Arbitraries.of(
                new Normalization("J.Doe", "j.doe"),
                new Normalization("USER", "user")
        );
    }
}
