package by.karpov.xmlparse.exception;

import by.karpov.xmlparse.periodicenum.PeriodicalEnum;

public class PeriodicalException extends RuntimeException {

    public PeriodicalException(String message, Throwable cause) {
        super(message, cause);
    }


    public PeriodicalException(Class<PeriodicalEnum> declaringClass, String name) {
        super(declaringClass + name);
    }

    public PeriodicalException(String s, String tagName) {
        super(s + tagName);
    }
}
