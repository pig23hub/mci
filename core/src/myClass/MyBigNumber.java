package myClass;

public class MyBigNumber {

    public MyBigNumber() {}

    private long sumValue(String firstNumber, String secondNumber) {
        char[] result = new char[firstNumber.length() + 1];
        boolean tenInd = false;
        int valueFirst = 0, valueSecond = 0;
        int resultIndex = result.length;

        for (int i=0; i<firstNumber.length(); i++) {
            valueFirst = Short.parseShort(firstNumber.substring(i, i+1));
            System.out.print("Add "+valueFirst);
            try {
                valueSecond = Short.parseShort(secondNumber.substring(i, i+1));
                valueFirst += valueSecond;
                System.out.print(" and "+valueSecond+": ");
            } catch (Exception e) {
            	System.out.print(": ");
            }
            if (tenInd)
                ++valueFirst;
            if (valueFirst > 9) {
                tenInd = true;
                valueFirst -= 10;
                if (i < firstNumber.length()-1) {
                    System.out.println("Write "+valueFirst+", note 1");
                } else {
                    System.out.println("Write "+valueFirst);
                }
            } else {
                tenInd = false;
                System.out.println("Write "+valueFirst);
            }
            result[--resultIndex] = (char) (valueFirst + '0');
        }

        if (tenInd) {
            result[--resultIndex] = '1';
        }

        long finalResult = Long.parseLong(new String(result, resultIndex, result.length - resultIndex));
        System.out.println("Final result: "+finalResult);
        
        return finalResult;
    }

    public long sum(String stn1, String stn2) {
    	System.out.println("\nCalc: "+stn1+" + "+stn2);
        // reverse 2 string
        stn1 = new StringBuffer(stn1).reverse().toString();
        stn2 = new StringBuffer(stn2).reverse().toString();

        if (stn1.length() >= stn2.length()) {
            return sumValue(stn1, stn2);
        }
        
        return sumValue(stn2, stn1);
    }
}