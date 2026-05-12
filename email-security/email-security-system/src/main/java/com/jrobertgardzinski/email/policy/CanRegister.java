package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.config.port.EmailConfigPort;
import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.Constraints;
import com.jrobertgardzinski.util.constraint.Decision;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import java.util.List;

public class CanRegister {

    private final Constraints<Email> constraints;

    CanRegister(List<ErrorConstraint<Email>> errorConstraints, WarningConstraint<Email> warningConstraint) {
        this.constraints = new Constraints<>(errorConstraints, warningConstraint);
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

    public Decision evaluate(Email email) {
        return constraints.decide(email);
    }
}
