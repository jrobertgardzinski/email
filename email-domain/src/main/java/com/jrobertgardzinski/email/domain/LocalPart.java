package com.jrobertgardzinski.email.domain;

import java.util.Objects;

/** The local part of an email address — everything before '@'. */
public final class LocalPart {

    private final String value;

    LocalPart(String value) {
        this.value = value;
    }

    public static LocalPart of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Email local part must not be empty");
        }
        if (value.startsWith(".") || value.endsWith(".")) {
            throw new IllegalArgumentException("Email local part must not start or end with a dot: " + value);
        }
        return new LocalPart(value);
    }

    public String value() { return value; }

    public LocalPart normalize(String domain) {
        if (domain.equals("gmail.com") || domain.equals("googlemail.com")) {
            return new LocalPart(value.toLowerCase().replaceAll("\\+.*", "").replace(".", ""));
        }
        if (domain.startsWith("yahoo.")) {
            return new LocalPart(value.toLowerCase().replaceAll("-.*", ""));
        }
        if (domain.equals("outlook.com") || domain.equals("hotmail.com") || domain.equals("live.com")) {
            return new LocalPart(value.toLowerCase().replaceAll("\\+.*", ""));
        }
        if (domain.equals("icloud.com") || domain.equals("me.com") || domain.equals("mac.com")) {
            return new LocalPart(value.toLowerCase());
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalPart)) return false;
        LocalPart other = (LocalPart) o;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
