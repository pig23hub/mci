package com.posco.mci.myBigNumber;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvImportServiceTest {

    @Test
    void shouldAddTotalAndTotalVatColumnsAndFooterRow() {
        String csv = String.join("\n",
                "Item,Quantity,Unit Price,VAT",
                "A,2,10,10",
                "B,3,5,5");

        CsvImportService service = new CsvImportService();
        CsvImportResult result = service.processCsv(csv, 1, 2, 3);

        assertEquals(4, result.getRows().size());
        assertEquals("Item,Quantity,Unit Price,VAT,Total,Total VAT", result.getRows().get(0));
        assertEquals("A,2,10,10,20,22.0", result.getRows().get(1));
        assertEquals("B,3,5,5,15,15.75", result.getRows().get(2));
        assertEquals(",,,,35,37.75", result.getRows().get(3));
    }
}
