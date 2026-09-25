package myClass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MyBigNumberTest {

    private final MyBigNumber myBigNumber = new MyBigNumber();

    @Test
    void sum() {
    	assertEquals(791L, myBigNumber.sum("23", "768"));
    	assertEquals(1000L, myBigNumber.sum("999", "1"));
    	assertEquals(1000L, myBigNumber.sum("1", "999"));
    	assertEquals(991L, myBigNumber.sum("223", "768"));
        assertEquals(1091L, myBigNumber.sum("323", "768"));
    }
}
