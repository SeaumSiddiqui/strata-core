package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

public class InvalidPhoneException extends DomainException {

    public InvalidPhoneException(String phone) {
        super("Invalid phone format: " + phone);
    }
}
