package myClass;

public class ScratchHandler {

    public ScratchHandler() {
        // default constructor
    }

    public long sum(String stn1, String stn2) {
        String firstNumber = normalize(stn1);
        String secondNumber = normalize(stn2);

        if (firstNumber.equals("0") && secondNumber.equals("0")) {
            return 0L;
        }

        boolean firstIsNegative = firstNumber.startsWith("-");
        boolean secondIsNegative = secondNumber.startsWith("-");
        String absFirst = stripSign(firstNumber);
        String absSecond = stripSign(secondNumber);

        if (firstIsNegative && secondIsNegative) {
            return parseLongValue("-" + addAbsolute(absFirst, absSecond));
        }

        if (firstIsNegative != secondIsNegative) {
            int comparison = compareAbsolute(absFirst, absSecond);
            if (comparison == 0) {
                return 0L;
            }

            boolean negativeResult =
                    (firstIsNegative && comparison > 0)
                            || (secondIsNegative && comparison < 0);
            String difference =
                    comparison > 0 ? subtractAbsolute(absFirst, absSecond)
                            : subtractAbsolute(absSecond, absFirst);
            return negativeResult ? -parseLongValue(difference) : parseLongValue(difference);
        }

        return parseLongValue(addAbsolute(absFirst, absSecond));
    }

    private String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Number cannot be null.");
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Number cannot be empty.");
        }

        if (trimmed.startsWith("+")) {
            trimmed = trimmed.substring(1);
        }

        boolean negative = trimmed.startsWith("-");
        if (negative) {
            trimmed = trimmed.substring(1);
        }

        trimmed = trimmed.replaceFirst("^0+(?!$)", "");
        if (trimmed.isEmpty()) {
            return "0";
        }

        return negative ? "-" + trimmed : trimmed;
    }

    private String stripSign(String value) {
        if (value.startsWith("-")) {
            return value.substring(1);
        }
        return value;
    }

    private int compareAbsolute(String left, String right) {
        String leftNormalized = left.replaceFirst("^0+(?!$)", "");
        String rightNormalized = right.replaceFirst("^0+(?!$)", "");

        if (leftNormalized.length() != rightNormalized.length()) {
            return Integer.compare(leftNormalized.length(), rightNormalized.length());
        }

        return leftNormalized.compareTo(rightNormalized);
    }

    private String addAbsolute(String left, String right) {
        String leftNormalized = left.replaceFirst("^0+(?!$)", "");
        String rightNormalized = right.replaceFirst("^0+(?!$)", "");

        if (leftNormalized.isEmpty()) {
            leftNormalized = "0";
        }
        if (rightNormalized.isEmpty()) {
            rightNormalized = "0";
        }

        int carry = 0;
        StringBuilder result = new StringBuilder();
        int leftIndex = leftNormalized.length() - 1;
        int rightIndex = rightNormalized.length() - 1;

        while (leftIndex >= 0 || rightIndex >= 0 || carry > 0) {
            int leftDigit = leftIndex >= 0 ? leftNormalized.charAt(leftIndex) - '0' : 0;
            int rightDigit = rightIndex >= 0 ? rightNormalized.charAt(rightIndex) - '0' : 0;
            int total = leftDigit + rightDigit + carry;

            result.insert(0, total % 10);
            carry = total / 10;
            leftIndex--;
            rightIndex--;
        }

        return result.toString();
    }

    private String subtractAbsolute(String left, String right) {
        String leftNormalized = left.replaceFirst("^0+(?!$)", "");
        String rightNormalized = right.replaceFirst("^0+(?!$)", "");

        if (leftNormalized.equals(rightNormalized)) {
            return "0";
        }

        int borrow = 0;
        StringBuilder result = new StringBuilder();
        int leftIndex = leftNormalized.length() - 1;
        int rightIndex = rightNormalized.length() - 1;

        while (leftIndex >= 0) {
            int leftDigit = leftNormalized.charAt(leftIndex) - '0';
            int rightDigit = rightIndex >= 0 ? rightNormalized.charAt(rightIndex) - '0' : 0;
            int total = leftDigit - rightDigit - borrow;

            if (total < 0) {
                total += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            result.insert(0, total);
            leftIndex--;
            rightIndex--;
        }

        String answer = result.toString().replaceFirst("^0+(?!$)", "");
        return answer.isEmpty() ? "0" : answer;
    }

    private long parseLongValue(String value) {
        String normalized = value;
        if (normalized.startsWith("-0")) {
            normalized = "0";
        }

        try {
            return Long.parseLong(normalized);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Result is outside the valid range for long.", ex);
        }
    }
}
