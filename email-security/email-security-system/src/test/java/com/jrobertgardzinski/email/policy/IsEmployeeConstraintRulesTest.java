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
@Feature("Company domain")
class IsEmployeeConstraintRulesTest {

    private static final Set<DomainPart> COMPANY = Set.of(DomainPart.of("acme.com"), DomainPart.of("corp.org"));

    @Property(tries = 10)
    @Label("email outside company domains is not satisfied when restriction is active")
    void outsideDomainIsNotSatisfied(@ForAll("outsideEmails") String raw) {
        Allure.parameter("email", raw);
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(COMPANY);
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @Property(tries = 10)
    @Label("email from company domain is satisfied")
    void companyEmailIsSatisfied(@ForAll("companyEmails") String raw) {
        Allure.parameter("email", raw);
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(COMPANY);
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("any email is satisfied when company domains are empty")
    void anyEmailSatisfiedWhenNoDomainRestriction() {
        _IsEmployeeConstraint constraint = new _IsEmployeeConstraint(Set.of());
        assertThat(constraint.isSatisfied(Email.of("user@example.com"))).isTrue();
    }

    @Example
    @Label("error code is NOT_A_COMPANY_DOMAIN")
    void errorCode() {
        assertThat(new _IsEmployeeConstraint(COMPANY).code()).isEqualTo("NOT_A_COMPANY_DOMAIN");
    }

    @Provide
    Arbitrary<String> outsideEmails() {
        return Arbitraries.of("user@gmail.com", "user@example.com");
    }

    @Provide
    Arbitrary<String> companyEmails() {
        return Arbitraries.of("user@acme.com", "admin@corp.org");
    }
}
