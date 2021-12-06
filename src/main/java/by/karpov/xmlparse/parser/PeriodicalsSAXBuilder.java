
package by.karpov.xmlparse.parser;

import by.karpov.xmlparse.handler.PeriodicalHandler;
import by.karpov.xmlparse.periodic.Periodical;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Set;


public class PeriodicalsSAXBuilder {
    static private final Logger LOGGER = LogManager.getLogger(PeriodicalsSAXBuilder.class);
    private Set<Periodical> periodicals;
    private final PeriodicalHandler handler;
    private XMLReader reader;

    public PeriodicalsSAXBuilder() {
        handler = new PeriodicalHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            reader = saxParser.getXMLReader();
            reader.setContentHandler(handler);
        } catch (SAXException | ParserConfigurationException e) {
            LOGGER.error("SAX parser error: " + e.getMessage());
        }
    }

    public Set<Periodical> getPeriodicals() {
        Set<Periodical> periodicals = this.periodicals;
        return periodicals;
    }

    public void buildSetPeriodicals(String fileName) {
        try {
            reader.parse(fileName);
        } catch (SAXException e) {
            LOGGER.error("SAX parser error: " + e);
        } catch (IOException e) {
            LOGGER.error("IO stream error: " + e);
        }
        periodicals = handler.getPeriodicals();
    }
}
