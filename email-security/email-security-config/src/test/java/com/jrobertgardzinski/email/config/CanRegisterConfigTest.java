package com.jrobertgardzinski.email.config;

import io.qameta.allure.Allure;
import net.jqwik.api.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CanRegisterConfigTest {

    private static final List<String> FIELD_NAMES = List.of("blockedDomains", "disposableDomains", "companyDomains");

    @Example
    @Label("default config → all domain sets are empty")
    void defaultConfig() {
        CanRegisterConfig config = new CanRegisterConfig();
        Allure.parameter("default", config);

        assertThat(config.blockedDomains().values()).isEmpty();
        assertThat(config.disposableDomains().values()).isEmpty();
        assertThat(config.companyDomains().values()).isEmpty();
    }

    @Property
    @Label("any null argument → IllegalArgumentException")
    void nullArgumentThrows(@ForAll("nullPositions") Set<Integer> nulls) {
        Allure.parameter("null fields", nulls.stream().map(FIELD_NAMES::get).toList());

        BlockedDomains    b = nulls.contains(0) ? null : new BlockedDomains(Set.of());
        DisposableDomains d = nulls.contains(1) ? null : new DisposableDomains(Set.of());
        CompanyDomains    c = nulls.contains(2) ? null : new CompanyDomains(Set.of());

        assertThatThrownBy(() -> new CanRegisterConfig(b, d, c))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Provide
    Arbitrary<Set<Integer>> nullPositions() {
        return Arbitraries.subsetOf(0, 1, 2).ofMinSize(1);
    }
}
