package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.Set;

class _BlockedDomainConstraint extends ErrorConstraint<Email> {

    static final String CODE = "DOMAIN_BLOCKED";
    private final Set<DomainPart> blockedDomains;

    _BlockedDomainConstraint(Set<DomainPart> blockedDomains) {
        this.blockedDomains = Set.copyOf(blockedDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !blockedDomains.contains(candidate.domain());
    }

    @Override
    public String code() {
        return CODE;
    }
}
