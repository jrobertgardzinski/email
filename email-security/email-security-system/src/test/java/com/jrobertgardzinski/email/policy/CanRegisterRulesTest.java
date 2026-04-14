package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.WarningConstraint;
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
@Feature("Can Register")
class CanRegisterRulesTest {

    private static final Email ANY_EMAIL = Email.of("user@example.com");

    private static final List<ErrorConstraint<Email>> CONSTRAINTS = List.of(
            alwaysFailing(new _RfcFormatConstraint()),
            alwaysFailing(new _BlockedDomainConstraint(Set.of())),
            alwaysFailing(new _DisposableEmailConstraint(Set.of())),
            alwaysFailing(new _IsEmployeeConstraint(Set.of(DomainPart.of("other.com"))))
    );

    @DisplayName("Rejects with ")
    @ParameterizedTest(name = "{1} when \"{0}\" is unsatisfied")
    @MethodSource("constraintCases")
    void unsatisfiedConstraintCausesRejection(String name, String code, ErrorConstraint<Email> constraint) {
        CanRegister policy = new CanRegister(List.of(constraint), passingMx());
        CanRegister.Decision decision = policy.evaluate(ANY_EMAIL);
        assertThat(decision).isInstanceOf(CanRegister.Decision.Rejected.class);
        assertThat(((CanRegister.Decision.Rejected) decision).errorCodes()).contains(code);
    }

    static Stream<Arguments> constraintCases() {
        return CONSTRAINTS.stream().map(c -> Arguments.of(c.toString(), c.code(), c));
    }

    @Property(tries = 10)
    @Label("if any subset of OTHER_CONSTRAINTS is unsatisfied → reject with all their codes")
    void anySubsetOfUnsatisfiedConstraintsCausesRejection(
            @ForAll("constraintCombinations") Set<ErrorConstraint<Email>> brokenConstraints) {
        List<String> expectedCodes = brokenConstraints.stream().map(ErrorConstraint::code).toList();
        Allure.parameter("OTHER_CONSTRAINTS", CONSTRAINTS);
        Allure.parameter("broken constraints", expectedCodes);

        CanRegister policy = new CanRegister(new ArrayList<>(brokenConstraints), passingMx());
        CanRegister.Decision decision = policy.evaluate(ANY_EMAIL);
        assertThat(decision).isInstanceOf(CanRegister.Decision.Rejected.class);
        assertThat(((CanRegister.Decision.Rejected) decision).errorCodes()).containsAll(expectedCodes);
    }

    @Example
    @Label("all constraints satisfied → allowed")
    void allConstraintsSatisfiedAllows() {
        CanRegister policy = new CanRegister(List.of(passing()), passingMx());

        assertThat(policy.evaluate(ANY_EMAIL)).isInstanceOf(CanRegister.Decision.Allowed.class);
    }

    @Example
    @Label("all error constraints satisfied, MX absent → allowed with \"NO_MX_RECORD\" warning")
    void noMxAllowsWithWarning() {
        CanRegister policy = new CanRegister(List.of(passing()), failingMx("NO_MX_RECORD"));

        CanRegister.Decision decision = policy.evaluate(ANY_EMAIL);

        assertThat(decision).isInstanceOf(CanRegister.Decision.AllowedWithWarning.class);
        assertThat(((CanRegister.Decision.AllowedWithWarning) decision).code()).isEqualTo("NO_MX_RECORD");
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

    private static WarningConstraint<Email> passingMx() {
        return new WarningConstraint<>() {
            @Override public boolean isSatisfied(Email e) { return true; }
            @Override public String code() { return "NO_MX_RECORD"; }
        };
    }

    private static WarningConstraint<Email> failingMx(String code) {
        return new WarningConstraint<>() {
            @Override public boolean isSatisfied(Email e) { return false; }
            @Override public String code() { return code; }
        };
    }
}
