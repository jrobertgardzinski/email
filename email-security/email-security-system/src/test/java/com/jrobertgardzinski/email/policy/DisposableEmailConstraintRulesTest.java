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
@Feature("Disposable email")
class DisposableEmailConstraintRulesTest {

    private static final Set<DomainPart> DISPOSABLE = Set.of(
            DomainPart.of("mailinator.com"),
            DomainPart.of("guerrillamail.com")
    );
    private final _DisposableEmailConstraint constraint = new _DisposableEmailConstraint(DISPOSABLE);

    @Property(tries = 10)
    @Label("email from disposable domain is not satisfied")
    void disposableDomainIsNotSatisfied(@ForAll("disposableEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isFalse();
    }

    @Property(tries = 10)
    @Label("email from non-disposable domain is satisfied")
    void nonDisposableDomainIsSatisfied(@ForAll("regularEmails") String raw) {
        Allure.parameter("email", raw);
        assertThat(constraint.isSatisfied(Email.of(raw))).isTrue();
    }

    @Example
    @Label("error code is DISPOSABLE_DOMAIN")
    void errorCode() {
        assertThat(constraint.code()).isEqualTo("DISPOSABLE_DOMAIN");
    }

    @Provide
    Arbitrary<String> disposableEmails() {
        return Arbitraries.of("user@mailinator.com", "temp@guerrillamail.com");
    }

    @Provide
    Arbitrary<String> regularEmails() {
        return Arbitraries.of("user@example.com", "user@gmail.com");
    }
}
