/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import org.eclipse.kapua.KapuaException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.*;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.sax.SAXSource;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;

import static org.apache.commons.lang.SystemUtils.LINE_SEPARATOR;

/**
 * Xml utilities
 *
 * @since 1.0
 */
public class XmlUtil {

    private static final Logger s_logger = LoggerFactory.getLogger(XmlUtil.class);

    private static JAXBContextProvider jaxbContextProvider;

    public static void setContextProvider(JAXBContextProvider provider) {
        jaxbContextProvider = provider;
    }

    /**
     * Marshal the object to a String
     *
     * @param object
     * @return
     * @throws JAXBException
     */
    public static String marshal(Object object)
            throws JAXBException {
        StringWriter sw = new StringWriter();
        marshal(object, sw);
        return sw.toString();
    }

    /**
     * Marshal the object to a writer
     *
     * @param object
     * @param w
     * @throws JAXBException
     */
    @SuppressWarnings("rawtypes")
    public static void marshal(Object object, Writer w)
            throws JAXBException {
        Class clazz = object.getClass();
        JAXBContext context = get(clazz);

        ValidationEventCollector valEventHndlr = new ValidationEventCollector();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(null);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setEventHandler(valEventHndlr);

        try {
            marshaller.marshal(object, w);
        } catch (Exception e) {
            if (e instanceof JAXBException) {
                throw (JAXBException) e;
            } else {
                throw new MarshalException(e.getMessage(), e);
            }
        }

        if (valEventHndlr.hasEvents()) {
            for (ValidationEvent valEvent : valEventHndlr.getEvents()) {
                if (valEvent.getSeverity() != ValidationEvent.WARNING) {
                    // throw a new Marshall Exception if there is a parsing error
                    throw new MarshalException(valEvent.getMessage(), valEvent.getLinkedException());
                }
            }
        }
    }

    /**
     * Unmashal the String to an object
     *
     * @param s
     * @param clazz
     * @return
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws SAXException
     */
    public static <T> T unmarshal(String s, Class<T> clazz)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        StringReader sr = new StringReader(s);
        return unmarshal(sr, clazz);
    }

    /**
     * Unmashal the reader to an object
     *
     * @param sr
     * @param clazz
     * @return
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws SAXException
     */
    public static <T> T unmarshal(Reader sr, Class<T> clazz)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        return unmarshal(sr, clazz, null);
    }

    /**
     * Unmarshal method which injects the namespace URI provided in all the elements before attempting the parsing.
     *
     * @param s
     * @param clazz
     * @param nsUri
     * @return
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws SAXException
     */
    public static <T> T unmarshal(String s, Class<T> clazz, String nsUri)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        StringReader sr = new StringReader(s);
        return unmarshal(sr, clazz, nsUri);
    }

    /**
     * Unmarshal method which injects the namespace URI provided in all the elements before attempting the parsing.
     *
     * @param r
     * @param clazz
     * @param nsUri
     * @return
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws SAXException
     */
    public static <T> T unmarshal(Reader r, Class<T> clazz, String nsUri)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        JAXBContext context = get(clazz);

        ValidationEventCollector valEventHndlr = new ValidationEventCollector();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(null);
        unmarshaller.setEventHandler(valEventHndlr);

        SAXSource saxSource;
        if (nsUri == null) {
            saxSource = new SAXSource(new InputSource(r));
        } else {
            boolean addNamespace = true;
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XmlNamespaceFilter filter = new XmlNamespaceFilter(nsUri, addNamespace);
            filter.setParent(reader);
            saxSource = new SAXSource(filter, new InputSource(r));
        }

        JAXBElement<T> elem = null;
        try {
            elem = unmarshaller.unmarshal(saxSource, clazz);
        } catch (JAXBException e) {
            throw e;
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage(), e);
        }

        if (valEventHndlr.hasEvents()) {
            for (ValidationEvent valEvent : valEventHndlr.getEvents()) {
                if (valEvent.getSeverity() != ValidationEvent.WARNING) {
                    // throw a new Unmarshall Exception if there is a parsing error
                    String msg = MessageFormat.format("Line {0}, Col: {1}.{2}\tError message: {3}\n\tLinked exception message:{4}",
                            valEvent.getLocator().getLineNumber(),
                            valEvent.getLocator().getColumnNumber(),
                            LINE_SEPARATOR,
                            valEvent.getMessage() != null ? valEvent.getMessage() : "",
                            valEvent.getLinkedException() != null ? valEvent.getLinkedException().getMessage() : "");
                    throw new UnmarshalException(msg, valEvent.getLinkedException());
                }
            }
        }
        return elem.getValue();
    }

    /**
     * Find child element by QName
     *
     * @param node
     * @param qname
     * @return
     */
    public static Element findChildElement(Node node, QName qname) {
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {

            Node n = nl.item(i);

            boolean isElement = n instanceof Element;
            boolean matchName = qname.getLocalPart().equals(n.getLocalName());
            boolean matchNsURI = qname.getNamespaceURI().isEmpty() ? (n.getNamespaceURI() == null || n.getNamespaceURI().isEmpty()) : qname.getNamespaceURI().equals(n.getNamespaceURI());

            if (isElement && matchName && matchNsURI) {
                return (Element) n;
            }
        }
        return null;
    }

    /**
     * Get the jaxb context for the provided class
     *
     * @param clazz
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("rawtypes")
    private static JAXBContext get(Class clazz) throws JAXBException {
        //        JAXBContext context = contexts.get(clazz);
        //        if (context == null) {
        //            context = JAXBContext.newInstance(clazz);
        //            contexts.put(clazz, context);
        //        }
        JAXBContext context;
        try {
            context = jaxbContextProvider.getJAXBContext();
            if (context == null) {
                s_logger.warn("No JAXBContext found; using ");
                context = JAXBContextFactory.createContext(new Class[] {}, null);
            }
        } catch (KapuaException | NullPointerException ex) {
            context = JAXBContextFactory.createContext(new Class[] {}, null);
            s_logger.warn("No JAXBContextProvider provided or error while getting one; using default JAXBContext");
        }
        return context;
    }
}
