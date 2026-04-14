package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Use case")
@Feature("Can Reset Password")
class CanResetPasswordRulesTest {

    private static final Email ANY_EMAIL = Email.of("user@example.com");

    private static final List<ErrorConstraint<Email>> CONSTRAINTS = List.of(
            alwaysFailing(new _RfcFormatConstraint()),
            alwaysFailing(new _BlockedDomainConstraint(Set.of()))
    );

    @DisplayName("Rejects with ")
    @ParameterizedTest(name = "{1} when \"{0}\" is unsatisfied")
    @MethodSource("constraintCases")
    void unsatisfiedConstraintCausesRejection(String name, String code, ErrorConstraint<Email> constraint) {
        CanResetPassword policy = new CanResetPassword(List.of(constraint));
        CanResetPassword.Decision decision = policy.evaluate(ANY_EMAIL);
        assertThat(decision).isInstanceOf(CanResetPassword.Decision.Rejected.class);
        assertThat(((CanResetPassword.Decision.Rejected) decision).errorCodes()).contains(code);
    }

    static Stream<Arguments> constraintCases() {
        return CONSTRAINTS.stream().map(c -> Arguments.of(c.toString(), c.code(), c));
    }

    @Property(tries = 10)
    @Label("if any subset of CONSTRAINTS is unsatisfied → reject with all their codes")
    void anySubsetOfUnsatisfiedConstraintsCausesRejection(
            @ForAll("constraintCombinations") Set<ErrorConstraint<Email>> brokenConstraints) {
        List<String> expectedCodes = brokenConstraints.stream().map(ErrorConstraint::code).toList();
        Allure.parameter("CONSTRAINTS", CONSTRAINTS);
        Allure.parameter("broken constraints", expectedCodes);

        CanResetPassword policy = new CanResetPassword(new ArrayList<>(brokenConstraints));
        CanResetPassword.Decision decision = policy.evaluate(ANY_EMAIL);
        assertThat(decision).isInstanceOf(CanResetPassword.Decision.Rejected.class);
        assertThat(((CanResetPassword.Decision.Rejected) decision).errorCodes()).containsAll(expectedCodes);
    }

    @Example
    @Label("all constraints satisfied → allowed")
    void allConstraintsSatisfiedAllows() {
        CanResetPassword policy = new CanResetPassword(List.of(passing()));
        assertThat(policy.evaluate(ANY_EMAIL)).isInstanceOf(CanResetPassword.Decision.Allowed.class);
    }

    @Provide
    Arbitrary<Set<ErrorConstraint<Email>>> constraintCombinations() {
        return Arbitraries.subsetOf(CONSTRAINTS).ofMinSize(1);
    }

    // --- stubs ---

    private static ErrorConstraint<Email> alwaysFailing(ErrorConstraint<Email> delegate) {
        String name = delegate.getClass().getSimpleName();
        return new ErrorConstraint<>() {
            @Override public boolean isSatisfied(Email e) { return false; }
            @Override public String code() { return delegate.code(); }
            @Override public String toString() { return name; }
        };
    }

    private static ErrorConstraint<Email> passing() {
        return new ErrorConstraint<>() {
            @Override public boolean isSatisfied(Email e) { return true; }
            @Override public String code() { return "UNUSED"; }
        };
    }
}
