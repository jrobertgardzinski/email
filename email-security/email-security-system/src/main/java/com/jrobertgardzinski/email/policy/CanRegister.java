package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.config.port.EmailConfigPort;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.Constraint;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import com.jrobertgardzinski.email.domain.DomainPart;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CanRegister {

    private final List<ErrorConstraint<Email>> errorConstraints;
    private final WarningConstraint<Email> warningConstraints;

    CanRegister(List<ErrorConstraint<Email>> errorConstraints, WarningConstraint<Email> warningConstraints) {
        this.errorConstraints = errorConstraints;
        this.warningConstraints = warningConstraints;
    }

    public static CanRegister configurable(EmailConfigPort emailConfigPort, MxRecordPort mxRecordPort) {
        return new CanRegister(
                List.of(
                        new _RfcFormatConstraint(),
                        new _DisposableEmailConstraint(emailConfigPort.disposableDomains().values()),
                        new _BlockedDomainConstraint(emailConfigPort.blockedDomains().values()),
                        new _IsEmployeeConstraint(emailConfigPort.companyDomains().values())
                ),
                new _MxRecordConstraint(mxRecordPort)
        );
    }

    private static Set<String> toStringSet(Set<DomainPart> domains) {
        return domains.stream().map(DomainPart::value).collect(Collectors.toSet());
    }

    public Decision evaluate(Email email) {
        List<String> codes = errorConstraints.stream()
                .filter(el -> !el.isSatisfied(email))
                .map(Constraint::code)
                .toList();

        if (!codes.isEmpty()) {
            return new Decision.Rejected(codes);
        }

        boolean mxRecordExists = warningConstraints.isSatisfied(email);

        return mxRecordExists ?
                new Decision.Allowed() :
                new Decision.AllowedWithWarning(warningConstraints.code());
    }

    public interface Decision {
        record Rejected(
                List<String> errorCodes) implements Decision {
        }

        record Allowed() implements Decision {}

        record AllowedWithWarning(String code) implements Decision {}
    }

}
