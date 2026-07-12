package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.Example;
import net.jqwik.api.Label;

import static com.jrobertgardzinski.email.policy.MxRecordConstraintRulesTest.CONFIG;
import static com.jrobertgardzinski.email.policy._MxRecordConstraint.CODE;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Email")
@Feature("Constraints")
@Story("MX record (for instance: fails on \"" + CONFIG + "\" domain)")
class MxRecordConstraintRulesTest {

    public static final String CONFIG = "no-mx.com";
    _MxRecordConstraint constraint = new _MxRecordConstraint(e -> !e.domain().equals(DomainPart.of(CONFIG)));

    final String REJECTS = "somebody@no-mx.com";
    @Example
    @Label("rejects \"" + REJECTS + "\"")
    void rejection() {
        assertThat(constraint.isSatisfied(Email.of(REJECTS))).isFalse();
    }

    final String ACCEPTS = "user@anything-else.com";
    @Example
    @Label("accepts \"" + ACCEPTS + "\"")
    void acceptance() {
        assertThat(constraint.isSatisfied(Email.of(ACCEPTS))).isTrue();
    }

    @Example
    @Label("error code is " + CODE)
    void errorCode() {
        assertThat(constraint.code()).isEqualTo(CODE);
    }
}
