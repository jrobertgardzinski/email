package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.Example;
import net.jqwik.api.Label;

import java.util.Set;

import static com.jrobertgardzinski.email.policy.BlockedDomainConstraintRulesTest.CONFIG;
import static com.jrobertgardzinski.email.policy._BlockedDomainConstraint.CODE;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Email")
@Feature("Constraints")
@Story("BlockedDomain (for instance: \"" + CONFIG + "\")")
class BlockedDomainConstraintRulesTest {

    public static final String CONFIG = "junk.org";
    _BlockedDomainConstraint constraint = new _BlockedDomainConstraint(Set.of(DomainPart.of(CONFIG)));

    final String REJECTS = "admin@junk.org";

    @Example
    @Label("rejects \"" + REJECTS + "\" (domain is on the blocked list)")
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
