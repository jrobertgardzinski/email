package com.jrobertgardzinski.email.config;

import com.jrobertgardzinski.email.domain.DomainPart;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import net.jqwik.api.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Email")
@Feature("Email Security Config - CanRegisterConfig")
class CanRegisterConfigTest {

    @Property
    @Label("Resolved: domain list from port")
    void domainListFromPort(@ForAll("portConfigs") Tuple.Tuple3<String, CanRegisterConfig, Set<DomainPart>> tc) {
        String type = tc.get1();
        Allure.parameter("type", type);
        CanRegisterConfig config = tc.get2();
        Set<DomainPart> expected = tc.get3();

        Set<DomainPart> actual = switch (type) {
            case "blocked"    -> config.blockedDomains().values();
            case "disposable" -> config.disposableDomains().values();
            case "company"    -> config.companyDomains().values();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Example
    @Label("Resolved: absent port entry defaults to empty set")
    void absentEntryDefaultsToEmptySet() {
        CanRegisterConfig config = new CanRegisterConfig(k -> null, k -> null, k -> null);

        assertThat(config.blockedDomains().values()).isEmpty();
        assertThat(config.disposableDomains().values()).isEmpty();
        assertThat(config.companyDomains().values()).isEmpty();
    }

    @Provide
    Arbitrary<Tuple.Tuple3<String, CanRegisterConfig, Set<DomainPart>>> portConfigs() {
        Set<DomainPart> blocked    = Set.of(DomainPart.of("spam.com"),       DomainPart.of("junk.org"));
        Set<DomainPart> disposable = Set.of(DomainPart.of("mailinator.com"), DomainPart.of("guerrillamail.com"));
        Set<DomainPart> company    = Set.of(DomainPart.of("acme.com"),       DomainPart.of("corp.org"));

        return Arbitraries.of(
                Tuple.of("blocked",    new CanRegisterConfig(k -> new BlockedDomains(blocked),       k -> null, k -> null), blocked),
                Tuple.of("disposable", new CanRegisterConfig(k -> null, k -> new DisposableDomains(disposable), k -> null), disposable),
                Tuple.of("company",    new CanRegisterConfig(k -> null, k -> null, k -> new CompanyDomains(company)),       company)
        );
    }
}
