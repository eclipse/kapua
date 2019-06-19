/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.experimental.categories.Category;
import org.xml.sax.Attributes;

import org.junit.Assert;
import org.junit.Test;

@Category(JUnitTests.class)
public class XmlNamespaceFilterTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        @SuppressWarnings("unused")
        XmlNamespaceFilter testConsturctor1 = new XmlNamespaceFilter("string", true);
        @SuppressWarnings("unused")
        XmlNamespaceFilter testConsturctor2 = new XmlNamespaceFilter("string", false);
    }

    @Test
    public void testStartDocument() throws Exception {
        XmlNamespaceFilter startDoc1 = new XmlNamespaceFilter("string", true);
        XmlNamespaceFilter startDoc2 = new XmlNamespaceFilter("string", false);
        startDoc1.startDocument();
        startDoc2.startDocument();
    }

    @Test
    public void testStartElement() throws Exception {
        Attributes attr = null;
        XmlNamespaceFilter startElement = new XmlNamespaceFilter("string", true);
        startElement.startElement("string", "string", "string", attr);
    }

    @Test
    public void testEndElement() throws Exception {
        XmlNamespaceFilter endElement = new XmlNamespaceFilter("string", true);
        endElement.endElement("string", "string", "string");
    }

    @Test
    public void testStartPrefixMapping() throws Exception {
        XmlNamespaceFilter prefixMap = new XmlNamespaceFilter("string", true);
        XmlNamespaceFilter prefixMap2 = new XmlNamespaceFilter("string", false);
        prefixMap.startPrefixMapping("string", "string");
        prefixMap2.startPrefixMapping("string", "string");
    }
}