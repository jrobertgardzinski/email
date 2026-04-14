package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.Example;
import net.jqwik.api.Label;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("Blocked domain")
class BlockedDomainConstraintRulesTest {

    @DisplayName("rejects ")
    @ParameterizedTest(name = "\"{0}\" (domain \"{1}\" is blocked)")
    @CsvSource({
            "admin@junk.org,   junk.org",
            "user@spammer.com, spammer.com"
    })
    void rejectsEmailFromBlockedDomain(String email, String blockedDomain) {
        _BlockedDomainConstraint constraint = new _BlockedDomainConstraint(Set.of(DomainPart.of(blockedDomain.trim())));
        assertThat(constraint.isSatisfied(Email.of(email.trim()))).isFalse();
    }

    @DisplayName("accepts ")
    @ParameterizedTest(name = "\"{0}\" (domain not blocked)")
    @CsvSource({
            "user@example.com, spammer.com",
            "user@gmail.com,   junk.org"
    })
    void acceptsEmailFromNonBlockedDomain(String email, String blockedDomain) {
        _BlockedDomainConstraint constraint = new _BlockedDomainConstraint(Set.of(DomainPart.of(blockedDomain.trim())));
        assertThat(constraint.isSatisfied(Email.of(email.trim()))).isTrue();
    }

    @Example
    @Label("error code is DOMAIN_BLOCKED")
    void errorCode() {
        assertThat(new _BlockedDomainConstraint(Set.of()).code()).isEqualTo("DOMAIN_BLOCKED");
    }
}
