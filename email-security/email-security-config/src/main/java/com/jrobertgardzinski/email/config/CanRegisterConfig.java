package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.config.domain.RepositoryKey;
import com.jrobertgardzinski.config.source.repository.RepositoryConfigPort;
import com.jrobertgardzinski.config.source.repository.RepositoryConfigSource;
import com.jrobertgardzinski.email.config.port.EmailConfigPort;

import java.util.Set;

public class CanRegisterConfig implements EmailConfigPort {

    private static final RepositoryKey<BlockedDomains>    BLOCKED_DOMAINS    = new RepositoryKey<>("blocked-domains");
    private static final RepositoryKey<DisposableDomains> DISPOSABLE_DOMAINS = new RepositoryKey<>("disposable-domains");
    private static final RepositoryKey<CompanyDomains>    COMPANY_DOMAINS    = new RepositoryKey<>("company-domains");

    private final RepositoryConfigPort<BlockedDomains>    blockedPort;
    private final RepositoryConfigPort<DisposableDomains> disposablePort;
    private final RepositoryConfigPort<CompanyDomains>    companyPort;

    public CanRegisterConfig(
            RepositoryConfigPort<BlockedDomains>    blockedPort,
            RepositoryConfigPort<DisposableDomains> disposablePort,
            RepositoryConfigPort<CompanyDomains>    companyPort) {
        this.blockedPort    = blockedPort;
        this.disposablePort = disposablePort;
        this.companyPort    = companyPort;
    }

    @Override
    public BlockedDomains blockedDomains() {
        return new RepositoryConfigSource<>(blockedPort)
                .resolve(BLOCKED_DOMAINS)
                .orElse(new BlockedDomains(Set.of()));
    }

    @Override
    public DisposableDomains disposableDomains() {
        return new RepositoryConfigSource<>(disposablePort)
                .resolve(DISPOSABLE_DOMAINS)
                .orElse(new DisposableDomains(Set.of()));
    }

    @Override
    public CompanyDomains companyDomains() {
        return new RepositoryConfigSource<>(companyPort)
                .resolve(COMPANY_DOMAINS)
                .orElse(new CompanyDomains(Set.of()));
    }
}
