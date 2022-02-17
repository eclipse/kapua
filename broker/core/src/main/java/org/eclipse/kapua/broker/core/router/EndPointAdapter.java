/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.router;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link CamelKapuaDefaultRouter} {@link EndPoint} adapter
 *
 * @since 1.0
 */
public class EndPointAdapter extends XmlAdapter<Element, List<EndPoint>> {

    private static final Logger logger = LoggerFactory.getLogger(EndPointAdapter.class);

    @Override
    public Element marshal(List<EndPoint> value) throws Exception {
        Document document = createDocument();
        for (EndPoint endPoint : value) {
            JaxbContextHandler.getMarshaller().marshal(endPoint, document);
        }
        DOMSource source = new DOMSource(document);
        return (Element) source.getNode();
    }

    @Override
    public List<EndPoint> unmarshal(Element element) throws Exception {
        List<EndPoint> endPointList = new ArrayList<>();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!(node instanceof Text)) {
                logger.debug("Node name: {}", node.getNodeName());
                endPointList.add((EndPoint) JaxbContextHandler.getUnmarshaller().unmarshal(node));
            }
        }
        return endPointList;
    }

    private Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Sonar java:S2755
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // Sonar java:S2755
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.newDocument();
    }

}

class JaxbContextHandler {

    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    static {
        JAXBContext jaxbContext;
        try {
            jaxbContext = new BrokerJAXBContextProvider().getJAXBContext();
            marshaller = jaxbContext.createMarshaller();
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (KapuaException | JAXBException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot initialize JaxbContext!");
        }
    }

    private JaxbContextHandler() {
    }

    public static Marshaller getMarshaller() {
        return marshaller;
    }

    public static Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

}
