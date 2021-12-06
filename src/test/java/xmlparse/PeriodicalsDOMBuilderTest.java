package xmlparse;

import by.karpov.xmlparse.parser.PeriodicalsDOMBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PeriodicalsDOMBuilderTest {

    private static PeriodicalsDOMBuilder builder;

    @BeforeClass
    public static void initBuilder() {
        builder = new PeriodicalsDOMBuilder();
        builder.buildSetPeriodicals("src/test/resources/test.xml");
    }

    @Test
    public void buildSetPeriodicalsTest() {
        assertEquals(16, builder.getPeriodicals().size());
    }

}
