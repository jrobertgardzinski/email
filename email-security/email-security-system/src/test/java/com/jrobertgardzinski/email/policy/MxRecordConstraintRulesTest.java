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
@Feature("MX record")
class MxRecordConstraintRulesTest {

    @DisplayName("rejects ")
    @ParameterizedTest(name = "\"{0}\" (no MX record)")
    @ValueSource(strings = {"user@ghost-domain.com", "test@nowhere.invalid"})
    void rejectsEmailWithNoMxRecord(String raw) {
        _MxRecordConstraint constraint = new _MxRecordConstraint(e -> false);
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @DisplayName("accepts ")
    @ParameterizedTest(name = "\"{0}\" (has MX record)")
    @ValueSource(strings = {"user@example.com", "user@gmail.com"})
    void acceptsEmailWithMxRecord(String raw) {
        _MxRecordConstraint constraint = new _MxRecordConstraint(e -> true);
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("error code is NO_MX_RECORD")
    void errorCode() {
        assertThat(new _MxRecordConstraint(e -> false).code()).isEqualTo("NO_MX_RECORD");
    }
}
