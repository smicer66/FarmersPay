package com.probase.fra.farmerspay.api.enums;

public enum ArtefactType {
    TRANSACTION("TRANSACTION"), BANK("BANK");



    public final String value;

    private ArtefactType(String value) {
        this.value = value;
    }

    public ArtefactType valueOfLabel(String label) {
        for (ArtefactType e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
