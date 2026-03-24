package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.config.port.EmailConfigPort;
import com.jrobertgardzinski.email.domain.DomainPart;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CanRegisterConfigSteps {

    private Set<DomainPart> blockedInRepo    = Set.of();
    private Set<DomainPart> disposableInRepo = Set.of();
    private Set<DomainPart> companyInRepo    = Set.of();

    private EmailConfigPort config;

    @Given("the repository contains {word} domains {string}")
    public void theRepositoryContainsDomains(String type, String csv) {
        Set<DomainPart> parts = csv.isEmpty() ? null : toDomainParts(csv);
        switch (type) {
            case "blocked"    -> blockedInRepo    = parts;
            case "disposable" -> disposableInRepo = parts;
            case "company"    -> companyInRepo    = parts;
            default -> throw new IllegalArgumentException("Unknown domain type: " + type);
        }
    }

    @When("registration configuration is resolved")
    public void registrationConfigurationIsResolved() {
        config = new CanRegisterConfig(
                n -> blockedInRepo    == null ? null : new BlockedDomains(blockedInRepo),
                n -> disposableInRepo == null ? null : new DisposableDomains(disposableInRepo),
                n -> companyInRepo    == null ? null : new CompanyDomains(companyInRepo)
        );
    }

    @Then("{word} domains are {string}")
    public void domainsAre(String type, String expected) {
        Set<DomainPart> values = valuesFor(type);
        if (expected.isEmpty()) {
            assertThat(values).isEmpty();
        } else {
            assertThat(values).containsExactlyInAnyOrder(toDomainParts(expected).toArray(DomainPart[]::new));
        }
    }

    private Set<DomainPart> valuesFor(String type) {
        return switch (type) {
            case "blocked"    -> config.blockedDomains().values();
            case "disposable" -> config.disposableDomains().values();
            case "company"    -> config.companyDomains().values();
            default -> throw new IllegalArgumentException("Unknown domain type: " + type);
        };
    }

    private static Set<DomainPart> toDomainParts(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .map(DomainPart::new)
                .collect(Collectors.toSet());
    }
}
