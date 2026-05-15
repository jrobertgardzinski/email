package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.config.port.EmailConfigPort;

public record CanRegisterConfig(
        BlockedDomains blockedDomains,
        DisposableDomains disposableDomains,
        CompanyDomains companyDomains) implements EmailConfigPort {

    public CanRegisterConfig() {
        this(null, null, null);
    }
}
