package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.DomainPart;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.Set;

class _IsEmployeeConstraint extends ErrorConstraint<Email> {

    private final Set<DomainPart> companyDomains;

    _IsEmployeeConstraint(Set<DomainPart> companyDomains) {
        this.companyDomains = Set.copyOf(companyDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return companyDomains.isEmpty() || companyDomains.contains(candidate.domain());
    }

    @Override
    public String code() {
        return "NOT_A_COMPANY_DOMAIN";
    }
}
