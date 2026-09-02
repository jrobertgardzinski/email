package com.jrobertgardzinski.email.config.port;

import com.jrobertgardzinski.email.config.BlockedDomains;
import com.jrobertgardzinski.email.config.CompanyDomains;
import com.jrobertgardzinski.email.config.DisposableDomains;

public interface EmailConfigPort {
    DisposableDomains disposableDomains();
    BlockedDomains blockedDomains();
    CompanyDomains companyDomains();
}
