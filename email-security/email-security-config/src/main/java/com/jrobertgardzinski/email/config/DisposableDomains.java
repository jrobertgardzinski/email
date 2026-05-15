package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.domain.DomainPart;

import java.util.Objects;
import java.util.Set;

public record DisposableDomains(Set<DomainPart> values) {
    public DisposableDomains {
        Objects.requireNonNull(values);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Set cannot be empty!");
        }
    }
}
