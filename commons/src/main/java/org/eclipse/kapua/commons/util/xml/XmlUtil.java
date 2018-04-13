/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.sax.SAXSource;

import org.eclipse.kapua.KapuaException;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Xml utilities
 *
 * @since 1.0.0
 */
public class XmlUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    private XmlUtil() {
    }

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

    public static String marshalJson(Object object)
            throws JAXBException {
        StringWriter sw = new StringWriter();
        marshalJson(object, sw);
        return sw.toString();
    }

    /**
     * Marshal the object to a writer
     *
     * @param object
     * @param w
     * @throws JAXBException
     */
    public static void marshal(Object object, Writer w)
            throws JAXBException {
        JAXBContext context = get();

        ValidationEventCollector valEventHndlr = new ValidationEventCollector();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(null);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setEventHandler(valEventHndlr);

        try {
            marshaller.marshal(object, w);
        } catch (JAXBException je) {
            throw je;
        } catch (Exception e) {
            throw new MarshalException(e.getMessage(), e);
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
     * Marshal the object to a writer in json format
     *
     * @param object
     * @param w
     * @throws JAXBException
     */
    public static void marshalJson(Object object, Writer w)
            throws JAXBException {
        JAXBContext context = get();

        ValidationEventCollector valEventHndlr = new ValidationEventCollector();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(null);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        marshaller.setEventHandler(valEventHndlr);

        try {
            marshaller.marshal(object, w);
        } catch (JAXBException je) {
            throw je;
        } catch (Exception e) {
            throw new MarshalException(e.getMessage(), e);
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

    public static <T> T unmarshalJson(String s, Class<T> clazz, String nsUri)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        StringReader sr = new StringReader(s);
        return unmarshalJson(sr, clazz, nsUri);
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
        JAXBContext context = get();

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
                            SystemUtils.LINE_SEPARATOR,
                            valEvent.getMessage() != null ? valEvent.getMessage() : "",
                            valEvent.getLinkedException() != null ? valEvent.getLinkedException().getMessage() : "");
                    throw new UnmarshalException(msg, valEvent.getLinkedException());
                }
            }
        }
        return elem.getValue();
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
    public static <T> T unmarshalJson(Reader r, Class<T> clazz, String nsUri)
            throws JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        JAXBContext context = get();

        ValidationEventCollector valEventHndlr = new ValidationEventCollector();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(null);
        unmarshaller.setEventHandler(valEventHndlr);
        unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

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
                            SystemUtils.LINE_SEPARATOR,
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
     * Get the default JAXB context available.
     *
     * @return The default JAXB context available.
     * @throws JAXBException
     */
    private static JAXBContext get() throws JAXBException {
        JAXBContext context;
        try {
            context = jaxbContextProvider.getJAXBContext();
            if (context == null) {
                logger.warn("No JAXBContext found! Creating one using JAXBContextFactory.createContext(...).");
                context = JAXBContextFactory.createContext(new Class[] {}, null);
            }
        } catch (KapuaException | NullPointerException ex) {
            logger.warn("No JAXBContextProvider provided or error while getting one! Creating one using JAXBContextFactory.createContext(...).", ex);
            context = JAXBContextFactory.createContext(new Class[] {}, null);
        }
        return context;
    }
}
