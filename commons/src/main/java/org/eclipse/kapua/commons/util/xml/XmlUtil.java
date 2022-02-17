/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import com.google.common.base.Strings;
import org.apache.commons.lang.SystemUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.kapua.KapuaException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Xml utilities
 *
 * @since 1.0.0
 */
public class XmlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private XmlUtil() {
    }

    private static JAXBContextProvider jaxbContextProvider;

    /**
     * Sets the {@link JAXBContextProvider} to use.
     *
     * @param provider The {@link JAXBContextProvider} to use.
     * @since 1.0.0
     */
    public static void setContextProvider(@NotNull JAXBContextProvider provider) {
        jaxbContextProvider = provider;
    }

    /**
     * Gets the default {@link JAXBContext} available.
     *
     * @return The default {@link JAXBContext} available.
     * @throws JAXBException See {@link JAXBContextFactory#createContext(Class[], Map)}
     * @since 1.0.0
     */
    private static JAXBContext getContext() throws JAXBException {
        JAXBContext context;
        try {
            context = jaxbContextProvider.getJAXBContext();

            if (context == null) {
                LOG.warn("No JAXBContext found! Creating one using JAXBContextFactory.createContext(...).");
                context = JAXBContextFactory.createContext(new Class[]{}, null);
            }
        } catch (KapuaException | NullPointerException ex) {
            LOG.warn("No JAXBContextProvider provided or error while getting one! Creating one using JAXBContextFactory.createContext(...).", ex);
            context = JAXBContextFactory.createContext(new Class[]{}, null);
        }
        return context;
    }

    //
    // Marshall
    //

    /**
     * Marshals the given {@link Object} to an XML {@link String}.
     *
     * @param object The {@link Object} to marshal.
     * @return The Json {@link String} representation of the object.
     * @throws JAXBException See {@link Marshaller#marshal(Object, Writer)}.
     * @since 1.0.0
     */
    public static String marshal(@NotNull Object object)
            throws JAXBException {
        try (StringWriter sw = new StringWriter()) {
            marshal(object, sw);
            return sw.toString();
        } catch (IOException ioe) {
            // This exception is thrown when operations are performed on a closed Writer.
            // This Writer is self-contained in this XmlUtil class and .marshal(...) is not closing it.
            // Therefore if this IOException occurs, something really bad happened.
            throw new IllegalStateException("XmlUtil.marshal(Object) Writer was found unexpectedly closed!", ioe);
        }
    }

    /**
     * Marshals the given XML {@link Object} into the given {@link Writer} as XML representation.
     *
     * @param object The {@link Object} to marshall.
     * @param writer The {@link Writer} to use.
     * @throws JAXBException See {@link Marshaller#marshal(Object, Writer)}
     * @since 1.0.0
     */
    public static void marshal(@NotNull Object object, @NotNull Writer writer)
            throws JAXBException {
        marshal(object, writer, Collections.emptyMap());
    }

    /**
     * Marshals the given {@link Object} to an JSON {@link String}.
     *
     * @param object The {@link Object} to marshal.
     * @return The Json {@link String} representation of the object.
     * @throws JAXBException See {@link Marshaller#marshal(Object, Writer)}.
     * @since 1.0.0
     */
    public static String marshalJson(@NotNull Object object)
            throws JAXBException {
        try (StringWriter sw = new StringWriter()) {
            marshalJson(object, sw);
            return sw.toString();
        } catch (IOException ioe) {
            // This exception is thrown when operations are performed on a closed Writer.
            // This Writer is self-contained in this XmlUtil class and .marshal(...) is not closing it.
            // Therefore if this IOException occurs, something really bad happened.
            throw new IllegalStateException("XmlUtil.marshalJson(Object) Writer was found unexpectedly closed!", ioe);
        }
    }

    /**
     * Marshals the given XML {@link Object} into the given {@link Writer} as JSON representation.
     *
     * @param object The {@link Object} to marshall.
     * @param writer The {@link Writer} to use.
     * @throws JAXBException See {@link Marshaller#marshal(Object, Writer)}
     * @since 1.0.0
     */
    public static void marshalJson(@NotNull Object object, @NotNull Writer writer)
            throws JAXBException {

        Map<String, Object> jsonProperties = new HashMap<>();
        jsonProperties.put(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        jsonProperties.put(MarshallerProperties.MEDIA_TYPE, "application/json");

        marshal(object, writer, jsonProperties);
    }

    /**
     * Marshals the given {@link Object} into the given {@link Writer}, configuring the
     * {@link Marshaller} with the given additional {@link Properties}.
     *
     * @param object               The {@link Object} to marshall.
     * @param writer               The {@link Writer} to use.
     * @param additionalProperties Additional {@link Properties} to configure the {@link Marshaller}
     * @throws JAXBException See {@link Marshaller#marshal(Object, Writer)}
     * @since 1.5.0
     */
    private static void marshal(@NotNull Object object, @NotNull Writer writer, @NotNull Map<String, Object> additionalProperties) throws JAXBException {

        JAXBContext context = getContext();

        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(null);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        additionalProperties.forEach((key, value) -> {
            try {
                marshaller.setProperty(key, value);
            } catch (PropertyException e) {
                LOG.error("Marshaller invalid property: {}", key, e);
            }
        });

        ValidationEventCollector eventCollector = new ValidationEventCollector();
        marshaller.setEventHandler(eventCollector);

        try {
            marshaller.marshal(object, writer);
        } catch (JAXBException je) {
            throw je;
        } catch (Exception e) {
            throw new MarshalException(e.getMessage(), e);
        }

        if (eventCollector.hasEvents()) {
            for (ValidationEvent valEvent : eventCollector.getEvents()) {
                if (valEvent.getSeverity() != ValidationEvent.WARNING) {
                    throw new MarshalException(valEvent.getMessage(), valEvent.getLinkedException());
                }
            }
        }
    }

    //
    // Unmarshall
    //

    /**
     * Unmarshals the given XML {@link String} as the given {@link Class type}.
     *
     * @param objectString The {@link String} serialized.
     * @param type         The {@link Class} type to unmarshal to.
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshal(@NotNull String objectString, @NotNull Class<T> type)
            throws JAXBException, SAXException {
        try (Reader reader = new StringReader(objectString)) {
            return unmarshal(reader, type);
        } catch (IOException ioe) {
            // This exception is thrown when operations are performed on a closed Reader.
            // This Reader is self-contained in this XmlUtil class and .unmarshal(...) is not closing it.
            // Therefore if this IOException occurs, something really bad happened.
            throw new IllegalStateException("XmlUtil.unmarshal(String, Class) Reader was found unexpectedly closed!", ioe);
        }
    }

    /**
     * Unmarshals the given XML {@link Reader} as the given {@link Class type}.
     *
     * @param reader The source {@link Reader}.
     * @param type   The {@link Class} type to unmarshal to.
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshal(@NotNull Reader reader, @NotNull Class<T> type)
            throws JAXBException, SAXException {
        return unmarshal(reader, type, null);
    }

    /**
     * Unmarshals the given XML {@link String} as the given {@link Class type} according to the given namespace {@link java.net.URI}
     *
     * @param objectString The {@link String} serialized.
     * @param type         The {@link Class} type to unmarshal to.
     * @param namespaceUri The namespace {@link java.net.URI} to use
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshal(@NotNull String objectString, @NotNull Class<T> type, @Nullable String namespaceUri)
            throws JAXBException, SAXException {
        try (Reader reader = new StringReader(objectString)) {
            return unmarshal(reader, type, namespaceUri);
        } catch (IOException ioe) {
            // This exception is thrown when operations are performed on a closed Reader.
            // This Reader is self-contained in this XmlUtil class and .unmarshal(...) is not closing it.
            // Therefore if this IOException occurs, something really bad happened.
            throw new IllegalStateException("XmlUtil.unmarshal(String, Class, String) Reader was found unexpectedly closed!", ioe);
        }
    }


    /**
     * Unmarshals the given XML {@link Reader} as the given {@link Class type} according to the given namespace {@link java.net.URI}.
     *
     * @param reader       The source {@link Reader}.
     * @param type         The {@link Class} type to unmarshal to.
     * @param namespaceUri The namespace {@link java.net.URI} to use
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshal(@NotNull Reader reader, @NotNull Class<T> type, @Nullable String namespaceUri)
            throws JAXBException, SAXException {
        return unmarshal(reader, type, namespaceUri, Collections.emptyMap());
    }

    /**
     * Unmarshals the given JSON {@link String} as the given {@link Class type}
     *
     * @param objectString The {@link String} serialized.
     * @param type         The {@link Class} type to unmarshal to.
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.5.0
     */
    public static <T> T unmarshalJson(@NotNull String objectString, @NotNull Class<T> type) throws JAXBException, SAXException {
        return unmarshalJson(objectString, type, null);
    }

    /**
     * Unmarshals the given JSON {@link String} as the given {@link Class type} according to the given namespace {@link java.net.URI}.
     *
     * @param objectString The {@link String} serialized.
     * @param type         The {@link Class} type to unmarshal to.
     * @param namespaceUri The namespace {@link java.net.URI} to use
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshalJson(@NotNull String objectString, @NotNull Class<T> type, @Nullable String namespaceUri)
            throws JAXBException, SAXException {
        try (Reader reader = new StringReader(objectString)) {
            return unmarshalJson(reader, type, namespaceUri);
        } catch (IOException ioe) {
            // This exception is thrown when operations are performed on a closed Reader.
            // This Reader is self-contained in this XmlUtil class and .unmarshal(...) is not closing it.
            // Therefore if this IOException occurs, something really bad happened.
            throw new IllegalStateException("XmlUtil.unmarshalJson(String, Class, String) Reader was found unexpectedly closed!", ioe);
        }
    }

    /**
     * Unmarshals the given JSON {@link Reader} as the given {@link Class type} according to the given namespace {@link java.net.URI}.
     *
     * @param reader       The {@link Reader} serialized.
     * @param type         The {@link Class} type to unmarshal to.
     * @param namespaceUri The namespace {@link java.net.URI} to use
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     * @since 1.0.0
     */
    public static <T> T unmarshalJson(@NotNull Reader reader, @NotNull Class<T> type, @Nullable String namespaceUri)
            throws JAXBException, SAXException {

        Map<String, Object> jsonProperties = new HashMap<>();
        jsonProperties.put(MarshallerProperties.MEDIA_TYPE, "application/json");
        jsonProperties.put(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        return unmarshal(reader, type, namespaceUri, jsonProperties);
    }


    /**
     * Unmarshals the given JSON {@link Reader} as the given {@link Class type} according to the given namespace {@link java.net.URI},
     * configuring the {@link Unmarshaller} with the given additional {@link Properties}.
     *
     * @param reader               The {@link Reader} serialized.
     * @param type                 The {@link Class} type to unmarshal to.
     * @param namespaceUri         The namespace {@link java.net.URI} to use
     * @param additionalProperties Additional {@link Properties} to configure the {@link Unmarshaller}
     * @param <T>                  The return {@link Class}
     * @return The unmarshalled {@link Object}.
     * @throws JAXBException See {@link #getContext()}.
     * @throws SAXException  See {@link XMLReaderFactory#createXMLReader()}.
     */
    private static <T> T unmarshal(@NotNull Reader reader, @NotNull Class<T> type, @Nullable String namespaceUri, @NotNull Map<String, Object> additionalProperties) throws JAXBException, SAXException {
        JAXBContext context = getContext();

        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(null);

        additionalProperties.forEach((key, value) -> {
            try {
                unmarshaller.setProperty(key, value);
            } catch (PropertyException e) {
                LOG.error("Unmarshaller invalid property: {}", key, e);
            }
        });

        ValidationEventCollector eventCollector = new ValidationEventCollector();
        unmarshaller.setEventHandler(eventCollector);

        SAXSource saxSource;
        if (Strings.isNullOrEmpty(namespaceUri)) {
            saxSource = new SAXSource(new InputSource(reader));
        } else {
            XmlNamespaceFilter filter = new XmlNamespaceFilter(namespaceUri, true);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            filter.setParent(xmlReader);

            saxSource = new SAXSource(filter, new InputSource(reader));
        }

        JAXBElement<T> jaxbElement;
        try {
            jaxbElement = unmarshaller.unmarshal(saxSource, type);
        } catch (JAXBException e) {
            throw e;
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage(), e);
        }

        if (eventCollector.hasEvents()) {
            for (ValidationEvent valEvent : eventCollector.getEvents()) {
                LOG.warn("Unmarshal Validation Event: {} - {}", valEvent.getSeverity(), valEvent.getMessage(), valEvent.getLinkedException());

                if (valEvent.getSeverity() != ValidationEvent.WARNING) {
                    String msg = MessageFormat.format("Line {0}, Col: {1}.{2}\tError message: {3}{2}\tLinked exception message:{4}",
                            valEvent.getLocator().getLineNumber(),
                            valEvent.getLocator().getColumnNumber(),
                            SystemUtils.LINE_SEPARATOR,
                            valEvent.getMessage() != null ? valEvent.getMessage() : "",
                            valEvent.getLinkedException() != null ? valEvent.getLinkedException().getMessage() : "");
                    throw new UnmarshalException(msg, valEvent.getLinkedException());
                }
            }
        }
        return jaxbElement.getValue();
    }
}
