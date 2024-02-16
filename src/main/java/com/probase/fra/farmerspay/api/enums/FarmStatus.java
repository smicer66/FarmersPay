package com.probase.fra.farmerspay.api.enums;

public enum FarmStatus {
    ACTIVE("ACTIVE"), APPROVED("APPROVED"), SOLD("SOLD"), LIQUIDATED("LIQUIDATED"), DELETED("DELETED");



    public final String value;

    private FarmStatus(String value) {
        this.value = value;
    }

    public FarmStatus valueOfLabel(String label) {
        for (FarmStatus e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }



}
