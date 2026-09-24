package com.posco.mci.myBigNumber;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyBigNumberController {

    private final CsvImportService csvImportService = new CsvImportService();

    @GetMapping("/hello")
    public String getMessage() {
        return "Xin chào từ Java Spring Boot API!";
    }

    private List<String> sumValue(String firstNumber, String secondNumber) {
        List<String> resultList = new ArrayList<>();
        StringBuffer result = new StringBuffer();
        boolean tenInd = false;

        for (int i=0;i<firstNumber.length();i++) {
            int valueFirst = Short.parseShort(firstNumber.substring(i, i+1));
            try {
                int valueSecond = Short.parseShort(secondNumber.substring(i, i+1));
                valueFirst += valueSecond;
            } catch (Exception e) {}
            if (tenInd)
                ++valueFirst;
            if (valueFirst > 9) {
                tenInd = true;
                if (i < firstNumber.length()-1) {
                    valueFirst -= 10;
                    resultList.add("Write "+valueFirst+", note 1");
                } else
                    resultList.add("Write "+valueFirst);
            } else {
                tenInd = false;
                resultList.add("Write "+valueFirst);
            }
            result.insert(0, valueFirst+"");
        }

        resultList.add(0, result.toString());

        return resultList;
    }

    private List<String> sum(String stn1, String stn2) {
        // reverse 2 string
        stn1 = new StringBuffer(stn1).reverse().toString();
        stn2 = new StringBuffer(stn2).reverse().toString();

        if (stn1.length() >= stn2.length())
            return sumValue(stn1, stn2);
        return sumValue(stn2, stn1);
    }

    @PostMapping("/sum")
    public List<String> sum(@RequestBody MyBigNumberDTO request) {
        return sum(request.getStn1(), request.getStn2());
    }

    @PostMapping(value = "/csv/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CsvImportResult importCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("quantityIndex") int quantityIndex,
            @RequestParam("unitPriceIndex") int unitPriceIndex,
            @RequestParam("vatIndex") int vatIndex) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required.");
        }

        String csvContent = new String(file.getBytes());
        return csvImportService.processCsv(csvContent, quantityIndex, unitPriceIndex, vatIndex);
    }
}