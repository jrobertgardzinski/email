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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Constraints")
@Feature("Company domain")
class IsEmployeeConstraintRulesTest {

    @DisplayName("rejects ")
    @ParameterizedTest(name = "\"{0}\" (only \"{1}\" is allowed)")
    @CsvSource({
            "user@gmail.com,   acme.com",
            "user@example.com, corp.org"
    })
    void rejectsEmailOutsideCompanyDomains(String email, String allowedDomain) {
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(Set.of(DomainPart.of(allowedDomain.trim())));
        assertThat(constraint.isSatisfied(Email.of(email.trim()))).isFalse();
    }

    @DisplayName("accepts ")
    @ParameterizedTest(name = "\"{0}\" (is company domain)")
    @ValueSource(strings = {"user@acme.com", "admin@corp.org"})
    void acceptsEmailFromCompanyDomain(String email) {
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(
                Set.of(DomainPart.of("acme.com"), DomainPart.of("corp.org")));
        assertThat(constraint.isSatisfied(Email.of(email))).isTrue();
    }

    @Example
    @Label("accepts any email when no company restriction is configured")
    void acceptsAnyEmailWhenNoRestriction() {
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(Set.of());
        assertThat(constraint.isSatisfied(Email.of("user@anything.com"))).isTrue();
    }

    @Example
    @Label("error code is NOT_A_COMPANY_DOMAIN")
    void errorCode() {
        assertThat(new _IsEmployeeConstraint(Set.of()).code()).isEqualTo("NOT_A_COMPANY_DOMAIN");
    }
}
