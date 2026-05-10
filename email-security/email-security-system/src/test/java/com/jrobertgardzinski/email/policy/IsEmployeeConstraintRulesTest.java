package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.Example;
import net.jqwik.api.Label;

import java.util.Set;

import static com.jrobertgardzinski.email.policy.IsEmployeeConstraintRulesTest.CONFIG;
import static com.jrobertgardzinski.email.policy._IsEmployeeConstraint.CODE;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Email")
@Feature("Constraints")
@Story("CompanyDomain (for instance: \"" + CONFIG + "\")")
class IsEmployeeConstraintRulesTest {

    public static final String CONFIG = "corp.org";
    _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(Set.of(DomainPart.of(CONFIG)));

    final String REJECTS = "somebody@public-vendor.com";
    @Example
    @Label("rejects \"" + REJECTS + "\" (domain is not on the company list)")
    void rejection() {
        assertThat(constraint.isSatisfied(Email.of(REJECTS))).isFalse();
    }

    final String ACCEPTS = "user@corp.org";
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
