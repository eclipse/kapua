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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class XmlNamespaceFilter extends XMLFilterImpl
{

    private String  usedNamespaceUri;
    private boolean addNamespace;

    // State variable
    private boolean addedNamespace = false;

    public XmlNamespaceFilter(String namespaceUri,
                              boolean addNamespace)
    {
        super();

        if (addNamespace)
            this.usedNamespaceUri = namespaceUri;
        else
            this.usedNamespaceUri = "";
        this.addNamespace = addNamespace;
    }

    @Override
    public void startDocument()
        throws SAXException
    {
        super.startDocument();
        if (addNamespace) {
            startControlledPrefixMapping();
        }
    }

    @Override
    public void startElement(String arg0, String arg1, String arg2,
                             Attributes arg3)
        throws SAXException
    {
        super.startElement(this.usedNamespaceUri, arg1, arg2, arg3);
    }

    @Override
    public void endElement(String arg0, String arg1, String arg2)
        throws SAXException
    {
        super.endElement(this.usedNamespaceUri, arg1, arg2);
    }

    @Override
    public void startPrefixMapping(String prefix, String url)
        throws SAXException
    {
        if (addNamespace) {
            this.startControlledPrefixMapping();
        }
        else {
            // Remove the namespace, i.e. don´t call startPrefixMapping for parent!
        }

    }

    private void startControlledPrefixMapping()
        throws SAXException
    {
        if (this.addNamespace && !this.addedNamespace) {
            // We should add namespace since it is set and has not yet been done.
            super.startPrefixMapping("", this.usedNamespaceUri);

            // Make sure we dont do it twice
            this.addedNamespace = true;
        }
    }
}
