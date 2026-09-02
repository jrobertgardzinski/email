package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.InvalidEmailException;
import com.jrobertgardzinski.util.constraint.Constraints;
import com.jrobertgardzinski.util.constraint.Outcome;

import java.util.List;
import java.util.function.Supplier;

/**
 * Builds the candidate address and evaluates the policy against it, reporting BOTH kinds of
 * refusal in one vocabulary.
 *
 * {@link Constraints#validate} catches a broken invariant and reports the exception's MESSAGE,
 * which put a sentence next to the constraints' codes in the same list. The translation happens
 * here rather than in {@code Constraints}, because this is the only place that knows both
 * vocabularies: the generic machinery stays generic, and {@code email-domain} keeps its zero
 * dependencies.
 */
final class _EmailCandidate {

    private _EmailCandidate() {}

    static Outcome<Email> evaluate(Constraints<Email> constraints, Supplier<Email> candidate) {
        Email email;
        try {
            email = candidate.get();
        } catch (InvalidEmailException refused) {
            return new Outcome.RejectedDueToInvariantBreakage<>(List.of(refused.code()));
        }
        return constraints.validate(() -> email);
    }
}
