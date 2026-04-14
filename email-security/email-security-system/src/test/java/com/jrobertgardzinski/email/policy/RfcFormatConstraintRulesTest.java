package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.Example;
import net.jqwik.api.Label;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("RFC format")
class RfcFormatConstraintRulesTest {

    private final _RfcFormatConstraint constraint = new _RfcFormatConstraint();

    @DisplayName("rejects ")
    @ParameterizedTest(name = "\"{0}\"")
    @ValueSource(strings = {
            "user test@example.com",
            "user(comment)@example.com",
            "user,extra@example.com"
    })
    void rejectsNonRfcEmail(String raw) {
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @DisplayName("accepts ")
    @ParameterizedTest(name = "\"{0}\"")
    @ValueSource(strings = {
            "user@example.com",
            "j.doe+alias@gmail.com",
            "user123@home.pl"
    })
    void acceptsRfcCompliantEmail(String raw) {
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("error code is RFC_FORMAT_INVALID")
    void errorCode() {
        assertThat(constraint.code()).isEqualTo("RFC_FORMAT_INVALID");
    }
}
