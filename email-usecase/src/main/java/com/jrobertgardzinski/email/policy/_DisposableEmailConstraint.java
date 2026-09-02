package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.Set;

/**
 * Fails an address from a disposable-mailbox domain.
 */
class _DisposableEmailConstraint extends ErrorConstraint<Email> {

    static final String CODE = "DISPOSABLE_DOMAIN";
    private final Set<DomainPart> disposableDomains;

    _DisposableEmailConstraint(Set<DomainPart> disposableDomains) {
        this.disposableDomains = Set.copyOf(disposableDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !disposableDomains.contains(candidate.domain());
    }

    @Override
    public String code() {
        return CODE;
    }
}
