package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.config.domain.RepositoryKey;
import com.jrobertgardzinski.config.source.repository.RepositoryConfigPort;
import com.jrobertgardzinski.config.source.repository.RepositoryConfigSource;
import com.jrobertgardzinski.email.config.port.EmailConfigPort;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public record CanRegisterConfig(
        BlockedDomains blockedDomains,
        DisposableDomains disposableDomains,
        CompanyDomains companyDomains) implements EmailConfigPort {

    public CanRegisterConfig {
        List<String> errors = new LinkedList<>();

        if (blockedDomains == null) {
            errors.add("blockedDomains cannot be null!");
        }
        if (disposableDomains == null) {
            errors.add("disposableDomains cannot be null!");
        }
        if (companyDomains == null) {
            errors.add("companyDomains cannot be null!");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    public CanRegisterConfig() {
        this(
                new BlockedDomains(Collections.emptySet()),
                new DisposableDomains(Collections.emptySet()),
                new CompanyDomains(Collections.emptySet())
        );
    }
}
