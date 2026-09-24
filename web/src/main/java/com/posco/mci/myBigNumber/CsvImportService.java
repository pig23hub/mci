package com.posco.mci.myBigNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CsvImportService {

    public CsvImportResult processCsv(String csvContent, int quantityIndex, int unitPriceIndex, int vatIndex) {
        if (csvContent == null || csvContent.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV content is empty.");
        }

        String[] rawLines = csvContent.replace("\r\n", "\n").replace('\r', '\n').split("\n", -1);
        List<String[]> rows = new ArrayList<>();

        for (String rawLine : rawLines) {
            if (rawLine == null || rawLine.trim().isEmpty()) {
                continue;
            }
            rows.add(splitCsvLine(rawLine));
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("CSV content is empty.");
        }

        int maxColumns = 0;
        for (String[] row : rows) {
            maxColumns = Math.max(maxColumns, row.length);
        }

        validateColumnIndex(quantityIndex, maxColumns, "quantity");
        validateColumnIndex(unitPriceIndex, maxColumns, "unit price");
        validateColumnIndex(vatIndex, maxColumns, "VAT");

        List<String> outputRows = new ArrayList<>();
        outputRows.add(buildHeader(rows.get(0), maxColumns));

        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal grandVatTotal = BigDecimal.ZERO;

        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            String[] originalRow = rows.get(rowIndex);
            String[] fixedRow = normalizeRow(originalRow, maxColumns);

            BigDecimal quantity = parseDecimal(fixedRow[quantityIndex]);
            BigDecimal unitPrice = parseDecimal(fixedRow[unitPriceIndex]);
            BigDecimal vatRate = parseDecimal(fixedRow[vatIndex]);

            BigDecimal total = quantity.multiply(unitPrice);
            BigDecimal totalVat = total.add(total.multiply(vatRate).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));

            grandTotal = grandTotal.add(total);
            grandVatTotal = grandVatTotal.add(totalVat);

            List<String> rowWithTotals = new ArrayList<>();
            for (String cell : fixedRow) {
                rowWithTotals.add(cell);
            }
            rowWithTotals.add(formatTotal(total));
            rowWithTotals.add(formatVatTotal(totalVat));
            outputRows.add(String.join(",", rowWithTotals));
        }

        List<String> footer = new ArrayList<>();
        for (int i = 0; i < maxColumns; i++) {
            footer.add("");
        }
        footer.add(formatTotal(grandTotal));
        footer.add(formatVatTotal(grandVatTotal));
        outputRows.add(String.join(",", footer));

        return new CsvImportResult(outputRows);
    }

    private void validateColumnIndex(int index, int maxColumns, String columnName) {
        if (index < 0 || index >= maxColumns) {
            throw new IllegalArgumentException("Invalid " + columnName + " column index: " + index + ". CSV has " + maxColumns + " columns.");
        }
    }

    private String buildHeader(String[] headerRow, int maxColumns) {
        String[] normalizedHeader = normalizeRow(headerRow, maxColumns);
        List<String> cells = new ArrayList<>();
        for (String cell : normalizedHeader) {
            cells.add(cell);
        }
        cells.add("Total");
        cells.add("Total VAT");
        return String.join(",", cells);
    }

    private String[] splitCsvLine(String line) {
        return line.split(",", -1);
    }

    private String[] normalizeRow(String[] row, int expectedLength) {
        String[] normalized = new String[expectedLength];
        for (int i = 0; i < expectedLength; i++) {
            normalized[i] = i < row.length ? row[i].trim() : "";
        }
        return normalized;
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        String cleaned = value.replace("$", "").replace("%", "").trim();
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private String formatTotal(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    private String formatVatTotal(BigDecimal value) {
        String formatted = value.stripTrailingZeros().toPlainString();
        if (!formatted.contains(".")) {
            return formatted + ".0";
        }
        return formatted;
    }
}
