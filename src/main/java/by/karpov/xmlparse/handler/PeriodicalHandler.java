
package by.karpov.xmlparse.handler;

import by.karpov.xmlparse.exception.PeriodicalException;
import by.karpov.xmlparse.periodic.Magazine;
import by.karpov.xmlparse.periodic.Newspaper;
import by.karpov.xmlparse.periodic.Periodical;
import by.karpov.xmlparse.periodicenum.PeriodicalEnum;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static by.karpov.xmlparse.periodicenum.PeriodicalEnum.*;


public class PeriodicalHandler extends DefaultHandler {

    static final int INDEX_ATTRIBUTE = 0;
    static final int PERIOD_ATTR = 1;
    private final Set<Periodical> periodicals;
    private Periodical current;
    private PeriodicalEnum currentEnum;
    private final EnumSet<PeriodicalEnum> periodicalEnums;

    public PeriodicalHandler() {
        periodicals = new HashSet<>();
        periodicalEnums = EnumSet.range(TITLE, GLOSSY);
    }

    public Set<Periodical> getPeriodicals() {
        Set<Periodical> periodicals = Collections.unmodifiableSet(this.periodicals);
        return periodicals;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (NEWSPAPER.getValue().equals(localName)) {
            current = new Newspaper();
            current.setIndex(attrs.getValue(INDEX_ATTRIBUTE).intern());
            if (attrs.getLength() == 2) {
                current.setPeriod(Periodical.Period.valueOf(attrs.getValue(PERIOD_ATTR).toUpperCase()));
            }
        } else if (MAGAZINE.getValue().equals(localName)) {
            current = new Magazine();
            current.setIndex(attrs.getValue(INDEX_ATTRIBUTE).intern());

            if (attrs.getLength() == 2) {
                current.setPeriod(Periodical.Period.valueOf(attrs.getValue(PERIOD_ATTR).toUpperCase()));
            }
        } else {
            PeriodicalEnum temp = PeriodicalEnum.valueOf(localName.toUpperCase());
            if (periodicalEnums != null && periodicalEnums.contains(temp)) {
                currentEnum = temp;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (NEWSPAPER.getValue().equals(localName) || MAGAZINE.getValue().equals(localName)) {
            periodicals.add(current);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String s = new String(ch, start, length).trim().intern();
        if (currentEnum != null) {
            switch (currentEnum) {
                case TITLE -> current.setTitle(s);
                case VOLUME -> current.setVolume(Integer.parseInt(s));
                case COLORED -> ((Newspaper) current).setColored(Boolean.parseBoolean(s));
                case GLOSSY -> ((Magazine) current).setGlossy(Boolean.parseBoolean(s));
                default -> throw new PeriodicalException(
                        currentEnum.getDeclaringClass(), currentEnum.name());
            }
        }
        currentEnum = null;
    }
}   
