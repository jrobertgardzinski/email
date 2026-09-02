package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

/**
 * Fails an address that is not RFC-shaped.
 */
class _RfcFormatConstraint extends ErrorConstraint<Email> {

    private static final Pattern RFC_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
                    "@" +
                    "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
                    "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );
    static final String CODE = "RFC_FORMAT_INVALID";

    @Override
    public boolean isSatisfied(Email candidate) {
        return RFC_PATTERN.matcher(candidate.value()).matches();
    }

    @Override
    public String code() {
        return CODE;
    }
}
