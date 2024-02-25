package com.probase.fra.farmerspay.api.enums;

public enum FarmersPayResponseCode {
    SUCCESS("0"), GENERAL_ERROR("1"), VALIDATION_FAILED("2"), EXPIRED_TOKEN("3"), INVALID_AUTH_TOKEN("4"), AUTH_FAIL("5"),
    PROCESS_FAILED("6"), UNAUTHORIZED("7"), NOT_FOUND("8");

    public final String label;

    private FarmersPayResponseCode(String label) {
        this.label = label;
    }

    public static FarmersPayResponseCode valueOfLabel(String label) {
        for (FarmersPayResponseCode e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
