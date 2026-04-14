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
@Feature("Disposable email")
class DisposableEmailConstraintRulesTest {

    @DisplayName("rejects ")
    @ParameterizedTest(name = "\"{0}\" (domain \"{1}\" is disposable)")
    @CsvSource({
            "user@mailinator.com,     mailinator.com",
            "temp@guerrillamail.com,  guerrillamail.com"
    })
    void rejectsDisposableEmail(String email, String disposableDomain) {
        _DisposableEmailConstraint constraint = new _DisposableEmailConstraint(Set.of(DomainPart.of(disposableDomain.trim())));
        assertThat(constraint.isSatisfied(Email.of(email.trim()))).isFalse();
    }

    @DisplayName("accepts ")
    @ParameterizedTest(name = "\"{0}\" (domain not disposable)")
    @CsvSource({
            "user@example.com, mailinator.com",
            "user@gmail.com,   guerrillamail.com"
    })
    void acceptsNonDisposableEmail(String email, String disposableDomain) {
        _DisposableEmailConstraint constraint = new _DisposableEmailConstraint(Set.of(DomainPart.of(disposableDomain.trim())));
        assertThat(constraint.isSatisfied(Email.of(email.trim()))).isTrue();
    }

    @Example
    @Label("error code is DISPOSABLE_DOMAIN")
    void errorCode() {
        assertThat(new _DisposableEmailConstraint(Set.of()).code()).isEqualTo("DISPOSABLE_DOMAIN");
    }
}
