package com.posco.mci.myBigNumber;

import java.util.List;

public class CsvImportResult {
    private List<String> rows;

    public CsvImportResult() {
    }

    public CsvImportResult(List<String> rows) {
        this.rows = rows;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }
}
