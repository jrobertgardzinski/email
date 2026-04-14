package com.jrobertgardzinski.email.domain;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Epic("Domain")
@Feature("Email")
class EmailTest {

    @Example
    @Label("Invariant: rejects null")
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> Email.of(null));
    }

    @Property
    @Label("Invariant: rejects invalid input")
    void rejectsInvalidInput(@ForAll("invalidInputs") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThrows(IllegalArgumentException.class, () -> Email.of(tc.get2()));
    }

    @Property
    @Label("Invariant: accepts valid addresses")
    void acceptsValidAddresses(@ForAll("validAddresses") Tuple.Tuple2<String, String> tc) {
        Allure.parameter(tc.get1(), tc.get2());
        assertThatCode(() -> Email.of(tc.get2())).doesNotThrowAnyException();
    }

    @Property
    @Label("Construction: local part preserves case, domain part is lowercased, and value joins them")
    void identityIsDerivedFromLocalAndDomain(
            @ForAll @AlphaChars @StringLength(min = 1, max = 20) String local,
            @ForAll("mixedCaseDomains") String domain
    ) {
        String raw = local + "@" + domain;
        Email email = Email.of(raw);

        assertThat(email.local().value())
                .as("Local part should preserve original case")
                .isEqualTo(local);

        assertThat(email.domain().value())
                .as("Domain part should be lowercased")
                .isEqualTo(domain.toLowerCase());

        assertThat(email.value())
                .as("Email value should be local@domain(lowercase)")
                .isEqualTo(local + "@" + domain.toLowerCase());
    }


        @DisplayName("Domain normalization: ")
        @ParameterizedTest(name = "normalizes \"{0}\" to lowercase \"{1}\"")
        @CsvSource({
                "GMAIL.COM, gmail.com",
                "Gmail.Com, gmail.com",
                "googlemail.com, googlemail.com"
        })
        void domainNormalizesToLowercase(String domainInput, String expectedDomain) {
            Email email = Email.of("user@" + domainInput);
            assertThat(email.domain().value()).isEqualTo(expectedDomain);
        }

        @Example
        @Label("Normalization: handles Gmail specific rules (dots, aliases, case)")
        void normalizesGmail() {
            Email email = Email.of("J.Doe+spam@gmail.com");

            assertThat(email.normalized()).isPresent();
            assertThat(email.normalized().get().value()).isEqualTo("jdoe");
            assertThat(email.normalizedValue()).isEqualTo("jdoe@gmail.com");
        }

    @Provide
    Arbitrary<String> mixedCaseDomains() {
        return Arbitraries.of("GMAIL.COM", "home.PL", "Booking.Co.Uk", "User.Com");
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> invalidInputs() {
        return Arbitraries.of(
                Tuple.of("blank", ""),
                Tuple.of("single space", " "),
                Tuple.of("missing @", "usergmail.com"),
                Tuple.of("multiple @", "user@@gmail.com"),
                Tuple.of("empty local", "@gmail.com"),
                Tuple.of("empty domain", "user@"),
                Tuple.of("domain without dot", "user@localhost")
        );
    }

    @Provide
    Arbitrary<Tuple.Tuple2<String, String>> validAddresses() {
        return Arbitraries.of(
                Tuple.of("simple", "user@gmail.com"),
                Tuple.of("with alias", "j.doe+spam@gmail.com"),
                Tuple.of("Polish TLD", "user@home.pl"),
                Tuple.of("compound TLD", "user@booking.co.uk")
        );
    }


}
