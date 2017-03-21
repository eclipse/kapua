/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
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
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.newDocument();

        documentBuilder.accept(doc);

        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer t = tf.newTransformer();

        t.transform(new DOMSource(doc), new StreamResult(writer));
    }
}
