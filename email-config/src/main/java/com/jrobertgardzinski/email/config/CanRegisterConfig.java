package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.config.port.EmailConfigPort;

/**
 * The domain rules registration is judged by; a {@code null} list is an absent rule.
 */
public record CanRegisterConfig(
        BlockedDomains blockedDomains,
        DisposableDomains disposableDomains,
        CompanyDomains companyDomains) implements EmailConfigPort {

    public CanRegisterConfig() {
        this(null, null, null);
    }
}
