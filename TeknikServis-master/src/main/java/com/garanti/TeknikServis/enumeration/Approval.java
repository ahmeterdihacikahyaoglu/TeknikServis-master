package com.garanti.TeknikServis.enumeration;

public enum Approval {
    RED(0, "REDDEDİLEN TEKLİF"),
    ONAY(1, "ONAYLANAN TEKLİF"),
    BEKLEYEN(2, "BEKLEYEN TEKLİF");



    private final int approvalType;
    private final String approvalName;

    Approval(int approvalType, String approvalName) {
        this.approvalType = approvalType;
        this.approvalName = approvalName;
    }

    public int getValue() {
        return approvalType;
    }
    public String getName(){
        return approvalName;
    }
    public static String getStringValue(int intValue) {
        for (Approval approval : Approval.values()) {
            if (approval.getValue() == intValue) {
                return approval.getName();
            }
        }
        return null;
    }

    public static boolean isValid(int code) {
        for (Approval approval : Approval.values()) {
            if (approval.getValue() == code) {
                return approval.getValue()==code;
            }
        }
        return false;
    }
}
