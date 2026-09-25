package myClass;

public class MyBigNumber {

    public MyBigNumber() {}

    private long sumValue(String firstNumber, String secondNumber) {
    	StringBuffer result = new StringBuffer();
        boolean tenInd = false;
        int valueFirst = 0, valueSecond = 0;

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
                if (i < firstNumber.length()-1) {
                    valueFirst -= 10;
                    System.out.println("Write "+valueFirst+", note 1");
                } else
                    System.out.println("Write "+valueFirst);
            } else {
                tenInd = false;
                System.out.println("Write "+valueFirst);
            }
			result.insert(0, valueFirst + "");
        }

        return Long.parseLong(result.toString());
    }

    public long sum(String stn1, String stn2) {
        // reverse 2 string
        stn1 = new StringBuffer(stn1).reverse().toString();
        stn2 = new StringBuffer(stn2).reverse().toString();

        if (stn1.length() >= stn2.length()) {
            return sumValue(stn1, stn2);
        }
        
        return sumValue(stn2, stn1);
    }

    public static void main(String[] args) {
        MyBigNumber myBigNumber = new MyBigNumber();
        String stn1 = "23";
        String stn2 = "768";
        System.out.println("Result of sum "+stn1+" and "+stn2+" is "+myBigNumber.sum(stn1, stn2)+"\n");
        
        stn1 = "223";
        stn2 = "768";
        System.out.println("Result of sum "+stn1+" and "+stn2+" is "+myBigNumber.sum(stn1, stn2)+"\n");
        
        stn1 = "323";
        stn2 = "768";
        System.out.println("Result of sum "+stn1+" and "+stn2+" is "+myBigNumber.sum(stn1, stn2)+"\n");
    }
}