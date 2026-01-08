package com.ardent.commerce.strata.shared.application;

public interface ApplicationService<Request, Response> {
    Response execute(Request request);
}
