package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.Constraints;
import com.jrobertgardzinski.util.constraint.Decision;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.List;

public class CanResetPassword {

    private final Constraints<Email> constraints;

    public CanResetPassword(List<ErrorConstraint<Email>> errorConstraints) {
        this.constraints = new Constraints<>(errorConstraints);
    }

    public Decision<Email> evaluate(Email email) {
        return constraints.decide(email);
    }
}
