
package by.karpov.xmlparse.parser;

import by.karpov.xmlparse.exception.PeriodicalException;
import by.karpov.xmlparse.periodic.Magazine;
import by.karpov.xmlparse.periodic.Newspaper;
import by.karpov.xmlparse.periodic.Periodical;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static by.karpov.xmlparse.periodicenum.PeriodicalEnum.*;


public class PeriodicalsDOMBuilder {
    static private final Logger LOGGER = LogManager.getLogger(PeriodicalsDOMBuilder.class);
    private final Set<Periodical> periodicals;
    private DocumentBuilder docBuilder;

    public PeriodicalsDOMBuilder() {
        periodicals = new HashSet<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("Parser configuration exception: " + e);
        }
    }

    public Set<Periodical> getPeriodicals() {
        Set<Periodical> periodicals = this.periodicals;
        return periodicals;
    }

    public void buildSetPeriodicals(String fileName) {
        try {
            Document doc = docBuilder.parse(fileName);
            Element root = doc.getDocumentElement();
            buildSetByTagName(NEWSPAPER.getValue(), root);
            buildSetByTagName(MAGAZINE.getValue(), root);

        } catch (IOException e) {
            LOGGER.error("File error or I/O error: " + e);
            throw new PeriodicalException("Problems with reading file.", e);
        } catch (SAXException e) {
            LOGGER.error("Parsing failure: " + e);
            throw new PeriodicalException("Problems with parsing.", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("uri is null" + e);
            throw new PeriodicalException("Problems with uri maybe is null.", e);
        }
    }

    protected Periodical buildPeriodical(Element periodicalElement) {
        Periodical periodical;
        String tagName = periodicalElement.getTagName();
        switch (tagName) {
            case "newspaper" -> {
                periodical = new Newspaper();
                ((Newspaper) periodical).setColored(Boolean.parseBoolean(
                        getElementTextContent(periodicalElement, COLORED.getValue())));
            }
            case "magazine" -> {
                periodical = new Magazine();
                ((Magazine) periodical).setGlossy(Boolean.parseBoolean(
                        getElementTextContent(periodicalElement, GLOSSY.getValue())));
            }
            default -> {
                throw new PeriodicalException("not found periodical element {}", tagName);
            }
        }

        periodical.setIndex(periodicalElement.getAttribute(INDEX.getValue()));
        if (periodicalElement.hasAttribute(PERIOD.getValue())) {
            periodical.setPeriod(Periodical.Period.valueOf(
                    periodicalElement.getAttribute(PERIOD.getValue()).toUpperCase()));
        }
        periodical.setTitle(getElementTextContent(periodicalElement, TITLE.getValue()));
        periodical.setVolume(Integer.parseInt(
                getElementTextContent(periodicalElement, VOLUME.getValue())));
        return periodical;
    }

    private void buildSetByTagName(String tagName, Element root) {
        LOGGER.debug((root.getElementsByTagName(tagName)).getLength());
        NodeList periodicalsList = root.getElementsByTagName(tagName);
        for (int i = 0; i < periodicalsList.getLength(); i++) {
            Element periodicalElement = (Element) periodicalsList.item(i);
            Periodical periodical = buildPeriodical(periodicalElement);
            periodicals.add(periodical);
        }
    }

    private static String getElementTextContent(Element element, String elementName) {
        NodeList nList = element.getElementsByTagName(elementName);
        Node node = nList.item(0);
        return node.getTextContent();
    }
}
