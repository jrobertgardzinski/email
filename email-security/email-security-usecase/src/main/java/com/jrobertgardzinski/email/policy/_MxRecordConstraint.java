package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

class _MxRecordConstraint extends WarningConstraint<Email> {

    static final String CODE = "NO_MX_RECORD";
    private final MxRecordPort mxRecordPort;

    _MxRecordConstraint(MxRecordPort mxRecordPort) {
        this.mxRecordPort = mxRecordPort;
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return mxRecordPort.hasMxRecord(candidate);
    }

    @Override
    public String code() {
        return CODE;
    }
}
