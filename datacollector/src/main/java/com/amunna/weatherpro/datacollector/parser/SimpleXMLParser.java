package com.amunna.weatherpro.datacollector.parser;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@Singleton
public final class SimpleXMLParser {

    public Map<String, String> parseAndGetData(String xmlString) {
        Map<String, String> weatherData = Maps.newHashMap();
        try {
            xmlString = xmlString.replaceAll("yweather:forecast", "yweatherforecast");
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/rss/channel/item/yweatherforecast[1]";   //TODO check the whole list
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
                weatherData.put("date", namedNodeMap.getNamedItem("date").getNodeValue());
                weatherData.put("low", namedNodeMap.getNamedItem("low").getNodeValue());
                weatherData.put("high", namedNodeMap.getNamedItem("high").getNodeValue());
                weatherData.put("text", namedNodeMap.getNamedItem("text").getNodeValue());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            Throwables.propagate(e);
        } catch (IOException e) {
            Throwables.propagate(e);
        } catch (XPathExpressionException e) {
            Throwables.propagate(e);
        }
        return weatherData;
    }
}
