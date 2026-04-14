package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.Example;
import net.jqwik.api.Label;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("MX record")
class MxRecordConstraintRulesTest {

    @Example
    @Label("domain without MX record is not satisfied")
    void noMxRecordIsNotSatisfied() {
        _MxRecordConstraint constraint = new _MxRecordConstraint(e -> false);
        assertThat(constraint.isSatisfied(Email.of("user@ghost-domain.com"))).isFalse();
    }

    @Example
    @Label("domain with MX record is satisfied")
    void withMxRecordIsSatisfied() {
        _MxRecordConstraint constraint = new _MxRecordConstraint(e -> true);
        assertThat(constraint.isSatisfied(Email.of("user@example.com"))).isTrue();
    }

    @Example
    @Label("error code is NO_MX_RECORD")
    void errorCode() {
        assertThat(new _MxRecordConstraint(e -> false).code()).isEqualTo("NO_MX_RECORD");
    }
}
