package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.Constraints;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.Outcome;

import java.util.List;
import java.util.function.Supplier;

public class CanResetPassword {

    private final Constraints<Email> constraints;

    public CanResetPassword(List<ErrorConstraint<Email>> errorConstraints) {
        this.constraints = new Constraints<>(errorConstraints);
    }

    public Outcome<Email> evaluate(Supplier<Email> email) {
        return constraints.validate(email);
    }
}
