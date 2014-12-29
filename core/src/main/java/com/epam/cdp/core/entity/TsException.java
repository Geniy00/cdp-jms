package com.epam.cdp.core.entity;

/**
 * @author Geniy00
 */
public class TsException extends Exception {

    public enum Reason {
        WRONG_INPUT_JMS_MESSAGE_TYPE(100, "Wrong input message type. Input message must be of %s type"),
        CANNOT_PROCESS_JMS_MESSAGE(101, "Can't process jms message. %s");

        private final int code;

        private final String text;

        Reason(final int code, final String text) {
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public String getText() {
            return text;
        }

    }

    public TsException(final Reason reason, final String... params) {
        super(generateMessage(reason, params));
    }

    public TsException(final Throwable cause, final Reason reason, final String... params) {
        super(generateMessage(reason, params), cause);
    }

    private static String generateMessage(final Reason reason, final String[] params) {
        return String.format("Code: %d. Message: %s", reason.getCode(), generateReasonMessage(reason, params));
    }

    private static String generateReasonMessage(final Reason reason, final String[] params) {
        return String.format(reason.getText(), params);
    }

}
