package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("Blocked domain")
class BlockedDomainConstraintRulesTest {

    private static final Set<DomainPart> BLOCKED = Set.of(DomainPart.of("spammer.com"), DomainPart.of("junk.org"));
    private final _BlockedDomainConstraint constraint = new _BlockedDomainConstraint(BLOCKED);

    @Property(tries = 10)
    @Label("email from blocked domain is not satisfied")
    void blockedDomainIsNotSatisfied(@ForAll("blockedEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @Property(tries = 10)
    @Label("email from non-blocked domain is satisfied")
    void nonBlockedDomainIsSatisfied(@ForAll("allowedEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("error code is DOMAIN_BLOCKED")
    void errorCode() {
        assertThat(constraint.code()).isEqualTo("DOMAIN_BLOCKED");
    }

    @Provide
    Arbitrary<String> blockedEmails() {
        return Arbitraries.of("user@spammer.com", "admin@junk.org");
    }

    @Provide
    Arbitrary<String> allowedEmails() {
        return Arbitraries.of("user@example.com", "user@gmail.com");
    }
}
