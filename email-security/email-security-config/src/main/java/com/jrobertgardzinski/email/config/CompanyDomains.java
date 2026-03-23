package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.domain.DomainPart;

import java.util.Set;

public record CompanyDomains(Set<DomainPart> values) {
}
