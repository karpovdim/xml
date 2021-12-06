
package by.karpov.xmlparse.periodic;


public abstract class Periodical {

    public enum Period {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, UNSPECIFIED
    }

    private String index;
    private Period period;
    private String title;
    private int volume;

    {
        period = Period.UNSPECIFIED;
        index = "0000-0000";
        title = "no title";
        volume = 0;
    }

    public Periodical() {
    }

    public static Periodical getEmptyPeriodical() {
        return new Periodical() {
        };
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean equalsPeriodical(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodical that = (Periodical) o;
        if (volume != that.volume) return false;
        if (index != null ? !index.equals(that.index) : that.index != null) return false;
        if (period != that.period) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + index.hashCode();
        result = 31 * result + period.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + volume;
        return result;
    }

    @Override
    public String toString() {
        String toString = String.format("\"%s\" %s %dp %s",
                title, index, volume, (period != Period.UNSPECIFIED ? period : ""));
        return toString;
    }
}
