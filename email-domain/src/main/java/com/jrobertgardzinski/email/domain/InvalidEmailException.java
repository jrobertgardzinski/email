package com.jrobertgardzinski.email.domain;

/**
 * A refused address, named by a CODE rather than only by a sentence.
 *
 * The invariants below used to travel as prose ("Email domain must contain at least one '.': wp")
 * while every policy constraint travelled as a code ("RFC_FORMAT_INVALID"), and both ended up in
 * the SAME list on the wire — half of it readable only by a person, half only by a client. A
 * caller had no way to tell which entry it could branch on, and a UI could only print whatever it
 * was handed. The domain names its own failures now; the sentence stays as the exception message,
 * where a log reader wants it.
 *
 * It extends {@link IllegalArgumentException} on purpose: existing callers that catch the
 * unchecked argument failure keep working, and only those who ask for {@link #code()} see more.
 */
public final class InvalidEmailException extends IllegalArgumentException {

    /** The whole address is missing. */
    public static final String EMAIL_BLANK = "EMAIL_BLANK";
    /** No single '@' with something on both sides of it. */
    public static final String EMAIL_FORMAT_INVALID = "EMAIL_FORMAT_INVALID";
    /** Nothing before the '@'. */
    public static final String LOCAL_PART_EMPTY = "LOCAL_PART_EMPTY";
    /** The part before the '@' starts or ends with a dot. */
    public static final String LOCAL_PART_DOT_AT_EDGE = "LOCAL_PART_DOT_AT_EDGE";
    /** The part before the '@' carries two dots in a row, which separates nothing. */
    public static final String LOCAL_PART_CONSECUTIVE_DOTS = "LOCAL_PART_CONSECUTIVE_DOTS";
    /** Nothing after the '@'. */
    public static final String DOMAIN_EMPTY = "DOMAIN_EMPTY";
    /** The domain carries no dot, so it can name no registrable host. */
    public static final String DOMAIN_MISSING_DOT = "DOMAIN_MISSING_DOT";

    private final String code;

    InvalidEmailException(String code, String message) {
        super(message);
        this.code = code;
    }

    /** The stable name of this refusal — what a client branches on and a UI translates. */
    public String code() {
        return code;
    }
}
