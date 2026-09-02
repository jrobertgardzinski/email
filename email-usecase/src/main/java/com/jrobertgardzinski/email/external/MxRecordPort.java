package com.jrobertgardzinski.email.external;

import com.jrobertgardzinski.email.domain.Email;

/**
 * Tells whether an address's domain can receive mail at all.
 */
public interface MxRecordPort {
    boolean hasMxRecord(Email email);
}