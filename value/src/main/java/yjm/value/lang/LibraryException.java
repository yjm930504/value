package yjm.value.lang;

import yjm.value.ValueValidate;

import java.io.PrintStream;
import java.io.PrintWriter;

public class LibraryException extends RuntimeException{

    public LibraryException() {
        super("LibraryException Created");
        ValueValidate.error(this);
    }

    public LibraryException(final String message) {
        super(message);
        ValueValidate.error(this);
    }

    public LibraryException(final String message, final Throwable cause) {
        super(message, cause);
        ValueValidate.error(this);
    }

    public LibraryException(final Throwable cause) {
        super(cause);
        ValueValidate.error(this);
    }

    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    public Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public synchronized Throwable initCause(final Throwable cause) {
        return super.initCause(cause);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(final PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(final PrintWriter s) {
        super.printStackTrace(s);
    }

    @Override
    public void setStackTrace(final StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
