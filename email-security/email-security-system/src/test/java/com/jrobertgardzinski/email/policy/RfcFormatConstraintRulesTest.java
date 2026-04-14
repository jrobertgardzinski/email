package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("RFC format")
class RfcFormatConstraintRulesTest {

    private final _RfcFormatConstraint constraint = new _RfcFormatConstraint();

    @Property(tries = 10)
    @Label("any email failing RFC pattern is not satisfied")
    void nonRfcEmailIsNotSatisfied(@ForAll("invalidRfcEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @Property(tries = 10)
    @Label("any RFC-compliant email is satisfied")
    void rfcCompliantEmailIsSatisfied(@ForAll("validRfcEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("error code is RFC_FORMAT_INVALID")
    void errorCode() {
        assertThat(constraint.code()).isEqualTo("RFC_FORMAT_INVALID");
    }

    @Provide
    Arbitrary<String> invalidRfcEmails() {
        // Pass Email.of() structural check, fail RFC local-part charset
        return Arbitraries.of(
                "user test@example.com",
                "user(comment)@example.com",
                "user,extra@example.com"
        );
    }

    @Provide
    Arbitrary<String> validRfcEmails() {
        return Arbitraries.of(
                "user@example.com",
                "j.doe+alias@gmail.com",
                "user123@home.pl"
        );
    }
}
