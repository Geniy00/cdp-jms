package com.epam.cdp.core.entity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TsExceptionTest {

    @DataProvider
    public Object[][] reasonDataProvider() {
        return new Object[][]{
                { TsException.Reason.WRONG_INPUT_JMS_MESSAGE_TYPE, new String[]{ "test" },
                        "Code: 100. Message: Wrong input message type. test" },
                { TsException.Reason.WRONG_INPUT_JMS_MESSAGE_TYPE, null,
                        "Code: 100. Message: Wrong input message type. null" }

        };
    }

    @Test(dataProvider = "reasonDataProvider")
    public void testReasons(final TsException.Reason reason, final String[] params, final String expectedMessage)
            throws Exception {
        final TsException tsException = new TsException(reason, params);
        final String actualMessage = tsException.getMessage();

        assertTrue(actualMessage.contains(String.valueOf(reason.getCode())));
        assertEquals(actualMessage, expectedMessage);
    }
}