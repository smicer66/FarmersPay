package com.probase.fra.farmerspay.api.enums;

public enum FarmBankAccountStatus {

    ACTIVE("ACTIVE"), DEACTIVATED("LIQUIDATED"), INACTIVE("INACTIVE"), DELETED("DELETED");



    public final String value;

    private FarmBankAccountStatus(String value) {
        this.value = value;
    }

    public FarmBankAccountStatus valueOfLabel(String label) {
        for (FarmBankAccountStatus e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
