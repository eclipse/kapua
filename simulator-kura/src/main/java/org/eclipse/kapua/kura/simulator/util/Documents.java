/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public final class Documents {

    private Documents() {
    }

    public static String create(final Consumer<Document> documentBuilder)
            throws ParserConfigurationException, TransformerException {
        final StringWriter sw = new StringWriter();
        create(documentBuilder, sw);
        return sw.toString();
    }

    public static void create(final Consumer<Document> documentBuilder, final Writer writer)
            throws ParserConfigurationException, TransformerException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Sonar java:S2755
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // Sonar java:S2755
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.newDocument();

        documentBuilder.accept(doc);

        final TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Sonar java:S2755
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Sonar java:S2755
        final Transformer t = tf.newTransformer();

        t.transform(new DOMSource(doc), new StreamResult(writer));
    }
}
