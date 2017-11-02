package com.fileloader.util;

public enum CSVFileFormat {
    ID(0),
    FROM_CURRENCY(1),
    TO_CURRENCY(2),
    DEAL_TIMESTAMP(3),
    DEAL_AMOUNT(4);

    private int sequence;

    CSVFileFormat(int squence) {
        this.sequence = squence;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
