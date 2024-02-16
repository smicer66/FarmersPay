package com.probase.fra.farmerspay.api.enums;

public enum UserRole {
    SYSTEM("SYSTEM"),
    FARMER("FARMER"),
    ADMINISTRATOR("ADMINISTRATOR");



    public final String value;

    private UserRole(String value) {
        this.value = value;
    }

    public UserRole valueOfLabel(String label) {
        for (UserRole e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }



}
