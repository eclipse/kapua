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

/**
 * Xml namespace filter implementation.<br>
 * This implementation adds only a namespace uri if enabled via addNamespace flag.
 * 
 * @since 1.0
 *
 */
public class XmlNamespaceFilter extends XMLFilterImpl
{

    /**
     * Namespace uri to be used in the mapping operations
     */
    private String  usedNamespaceUri;

    /**
     * Flag to choose if add or not the namespace uri
     */
    private boolean addNamespace;

    // State variable
    private boolean addedNamespace = false;

    /**
     * Constructor
     * 
     * @param namespaceUri
     * @param addNamespace
     */
    public XmlNamespaceFilter(String namespaceUri,
                              boolean addNamespace)
    {
        super();

        if (addNamespace) {
            this.usedNamespaceUri = namespaceUri;
        }
        else {
            this.usedNamespaceUri = "";
        }
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
        super.startElement(usedNamespaceUri, arg1, arg2, arg3);
    }

    @Override
    public void endElement(String arg0, String arg1, String arg2)
        throws SAXException
    {
        super.endElement(usedNamespaceUri, arg1, arg2);
    }

    @Override
    public void startPrefixMapping(String prefix, String url)
        throws SAXException
    {
        if (addNamespace) {
            startControlledPrefixMapping();
        }
        else {
            // Remove the namespace, i.e. don´t call startPrefixMapping for parent!
        }

    }

    private void startControlledPrefixMapping()
        throws SAXException
    {
        if (addNamespace && !addedNamespace) {
            // We should add namespace since it is set and has not yet been done.
            super.startPrefixMapping("", usedNamespaceUri);

            // Make sure we dont do it twice
            addedNamespace = true;
        }
    }
}
