package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.domain.DomainPart;

import java.util.Set;

public record BlockedDomains(Set<DomainPart> values) {
}
