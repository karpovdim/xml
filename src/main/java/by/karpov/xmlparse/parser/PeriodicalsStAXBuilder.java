
package by.karpov.xmlparse.parser;

import by.karpov.xmlparse.exception.PeriodicalException;
import by.karpov.xmlparse.periodic.Magazine;
import by.karpov.xmlparse.periodic.Newspaper;
import by.karpov.xmlparse.periodic.Periodical;
import by.karpov.xmlparse.periodicenum.PeriodicalEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PeriodicalsStAXBuilder {
    static private final Logger LOGGER = LogManager.getLogger(PeriodicalsStAXBuilder.class);
    private final HashSet<Periodical> periodicals = new HashSet<>();
    private final XMLInputFactory inputFactory;

    public PeriodicalsStAXBuilder() {
        inputFactory = XMLInputFactory.newInstance();
    }

    public Set<Periodical> getPeriodicals() {
        HashSet<Periodical> periodicals = this.periodicals;
        return periodicals;
    }

    public void buildSetPeriodicals(String fileName) {
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "newspaper" -> periodicals.add(buildPeriodical(reader, new Newspaper()));
                        case "magazine" -> periodicals.add(buildPeriodical(reader, new Magazine()));
                    }
                }
            }
        } catch (XMLStreamException e) {
            LOGGER.error("StAX parsing error! " + e.getMessage());
        } catch (FileNotFoundException e) {
            LOGGER.error("File " + fileName + " not found! " + e);
        } catch (IOException e) {
            LOGGER.error("Error while closing InputStream" + e);
        }
    }

    protected Periodical buildPeriodical(XMLStreamReader reader, Periodical periodical) throws XMLStreamException {
        periodical.setIndex(reader.getAttributeValue(null, PeriodicalEnum.INDEX.getValue()));
        if (reader.getAttributeCount() == 2) {
            periodical.setPeriod(Periodical.Period.valueOf(
                    reader.getAttributeValue(null, PeriodicalEnum.PERIOD.getValue()).toUpperCase()));
        }
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    switch (PeriodicalEnum.valueOf(name.toUpperCase())) {
                        case TITLE:
                            periodical.setTitle(getXMLText(reader));
                            break;
                        case VOLUME:
                            periodical.setVolume(Integer.parseInt(getXMLText(reader)));
                            break;
                        case COLORED:
                            ((Newspaper) periodical).setColored(Boolean.parseBoolean(getXMLText(reader)));
                            break;
                        case GLOSSY:
                            ((Magazine) periodical).setGlossy(Boolean.parseBoolean(getXMLText(reader)));
                            break;
                        default:
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (PeriodicalEnum.valueOf(reader.getLocalName().toUpperCase()) == PeriodicalEnum.NEWSPAPER ||
                            PeriodicalEnum.valueOf(reader.getLocalName().toUpperCase()) == PeriodicalEnum.MAGAZINE) {
                        return periodical;
                    }
            }
        }
        throw new PeriodicalException("Unknown element in -", this.getClass().getName());
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = null;
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}
