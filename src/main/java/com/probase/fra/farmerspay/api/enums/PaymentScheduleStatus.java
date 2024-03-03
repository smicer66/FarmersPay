package com.probase.fra.farmerspay.api.enums;

public enum PaymentScheduleStatus {

    COMPLETED("COMPLETED"),
    RUNNING("RUNNING"),
    PENDING("PENDING"),
    APPROVED("APPROVED");



    public final String value;

    private PaymentScheduleStatus(String value) {
        this.value = value;
    }

    public PaymentScheduleStatus valueOfLabel(String label) {
        for (PaymentScheduleStatus e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
