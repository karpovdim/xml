package by.karpov.xmlparse.periodicenum;

public enum PeriodicalEnum {
    PERIODICALS("periodical"),
    INDEX("index"),
    PERIOD("period"),
    TITLE("title"),
    VOLUME("volume"),
    NEWSPAPER("newspaper"),
    COLORED("colored"),
    MAGAZINE("magazine"),
    GLOSSY("glossy");

    private String value;

    private PeriodicalEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
