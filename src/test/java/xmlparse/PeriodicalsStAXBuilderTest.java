package xmlparse;

import by.karpov.xmlparse.parser.PeriodicalsStAXBuilder;
import by.karpov.xmlparse.periodic.Magazine;
import by.karpov.xmlparse.periodic.Newspaper;
import by.karpov.xmlparse.periodic.Periodical;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class PeriodicalsStAXBuilderTest {

    private static PeriodicalsStAXBuilder builder;

    @BeforeClass
    public static void initBuilder() {
        builder = new PeriodicalsStAXBuilder();
        builder.buildSetPeriodicals("src/test/resources/test.xml");
    }

    @Test
    public void buildSetPeriodicalsTestSize() {
        assertEquals(16, builder.getPeriodicals().size());
    }
    
    @Test
    public void buildSetPeriodicalsTestFullElementNewspaper() {
        Newspaper news = new Newspaper();
        news.setIndex("2343-3422");
        news.setPeriod(Periodical.Period.WEEKLY);
        news.setTitle("The Daily Mash");
        news.setVolume(36);
        news.setColored(true);
        assertTrue(builder.getPeriodicals().contains(news));
    }
    
    @Test
    public void buildSetPeriodicalsTestFullElementMagazine() {
        Magazine mag = new Magazine();
        mag.setIndex("5436-4430");
        mag.setTitle("Cosmopolitan");
        mag.setVolume(94);
        mag.setGlossy(true);
        assertTrue(builder.getPeriodicals().contains(mag));
    }

}
