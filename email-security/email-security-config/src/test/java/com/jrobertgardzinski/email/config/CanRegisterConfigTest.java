package com.jrobertgardzinski.email.config;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.jqwik.api.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Epic("Email")
@Feature("Config")
@Story("CanRegister")
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
}
