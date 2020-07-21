package com.astraia.xml;

import com.astraia.components.*;
import com.astraia.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private static final Logger logger = LoggerFactory.getLogger(Transformer.class);

    private final Path filePath;

    private XMLParser(Path filePath) {
        this.filePath = filePath;
    }

    public static XMLParser parser(Path filePath) {
        return new XMLParser(filePath);
    }

    public Report parse() {
        Report report = new Report();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
            Document document = newDocumentBuilder.parse(filePath.toFile());
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getChildNodes().item(0).getChildNodes();
            List<Component> components = visitChildNodes(nodeList);
            report.setComponents(components);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            logger.error("Failed to parse xml file", e);
        }
        return report;
    }

    private List<Component> visitChildNodes(NodeList nodeList) {
        List<Component> components = new ArrayList<>();
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            Component component = null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                component = new ParentComponent(node.getNodeName());
                addAttributes(node, component);

                if (node.hasChildNodes()) {
                    component.getComponents().addAll(visitChildNodes(node.getChildNodes()));
                }

            } else if (node.getNodeType() == Node.TEXT_NODE) {
                if (!node.getTextContent().trim().isEmpty())
                    component = new TextComponent(node.getTextContent().replaceAll("\t", ""));
            }
            if (component != null)
                components.add(component);
        }
        return components;
    }

    private void addAttributes(Node node, Component component) {
        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node theAttribute = attributes.item(i);
                Component attributeComponent = new ParentComponent(theAttribute.getNodeName());
                attributeComponent.add(new TextComponent(theAttribute.getNodeValue()));
                component.add(attributeComponent);
            }
        }
    }

}
