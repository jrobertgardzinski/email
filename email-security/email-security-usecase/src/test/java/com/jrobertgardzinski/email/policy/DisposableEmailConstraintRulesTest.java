package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.Example;
import net.jqwik.api.Label;

import java.util.Set;

import static com.jrobertgardzinski.email.policy.DisposableEmailConstraintRulesTest.CONFIG;
import static com.jrobertgardzinski.email.policy._DisposableEmailConstraint.CODE;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Email")
@Feature("Constraints")
@Story("DisposableEmail (for instance: \"" + CONFIG + "\")")
class DisposableEmailConstraintRulesTest {

    public static final String CONFIG = "mailinator.com";
    _DisposableEmailConstraint constraint = new _DisposableEmailConstraint(Set.of(DomainPart.of(CONFIG)));

    final String REJECTS = "somebody@mailinator.com";
    @Example
    @Label("rejects \"" + REJECTS + "\" (domain is on the disposable list)")
    void rejection() {
        assertThat(constraint.isSatisfied(Email.of(REJECTS))).isFalse();
    }

    final String ACCEPTS = "user@example.com";
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
