package com.epam.cdp.core.entity;

/**
 * @author Geniy00
 */
public class TsException extends Exception {

    public enum Reason {
        WRONG_INPUT_JMS_MESSAGE_TYPE(100, "Wrong input message type. Input message must be of %s type."),
        CANNOT_PROCESS_JMS_MESSAGE(101, "Can't process jms message. %s"),

        PRE_CONDITION_CHECK_FAIL(110, "Precondition check fail. %s"),

        REST_REQUEST_EXECUTION_FAILURE(120, "Can't execute rest request. %s"),
        RESPONSE_PARSING_FAILURE(130, "Can't parse response:\n%s"),

        SERIALIZATION_FAILURE(140, "Serialization failure during serializing %s class"),
        DESERIALIZATION_FAILURE(140, "Deserialization failure during deserializing [%s] message to %s class"),

        ACTION_IS_UNAVAILABLE(150, "Can't execute an action %s"),

        ENUM_CONVERTING_ERROR(160, "Can't convert %s.%s to %s enum value");

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
