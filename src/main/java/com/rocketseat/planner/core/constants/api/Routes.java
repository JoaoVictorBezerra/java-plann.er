package com.rocketseat.planner.core.constants.api;

import com.rocketseat.planner.core.constants.errors.ExceptionsErrors;

public class Routes {

    private Routes() {
        throw new IllegalStateException(ExceptionsErrors.UTILITY_CLASS);
    }

    public static final String TRIP = "/api/trips";
    public static final String CREATE_TRIP = "/create-trip";


    public static final String PARTICIPANTS = "/api/participants";

}
