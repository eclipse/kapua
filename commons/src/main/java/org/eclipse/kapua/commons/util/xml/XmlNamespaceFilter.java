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
public class XmlNamespaceFilter extends XMLFilterImpl {

    /**
     * Namespace uri to be used in the mapping operations
     */
    private String usedNamespaceUri;

    /**
     * Flag to choose if add or not the namespace uri
     */
    private boolean addNamespace;

    // State variable
    private boolean addedNamespace;

    /**
     * Constructor
     *
     * @param namespaceUri
     * @param addNamespace
     */
    public XmlNamespaceFilter(String namespaceUri,
            boolean addNamespace) {
        super();

        if (addNamespace) {
            this.usedNamespaceUri = namespaceUri;
        } else {
            this.usedNamespaceUri = "";
        }
        this.addNamespace = addNamespace;
    }

    @Override
    public void startDocument()
            throws SAXException {
        super.startDocument();
        if (addNamespace) {
            startControlledPrefixMapping();
        }
    }

    @Override
    public void startElement(String arg0, String arg1, String arg2,
            Attributes arg3)
            throws SAXException {
        super.startElement(usedNamespaceUri, arg1, arg2, arg3);
    }

    @Override
    public void endElement(String arg0, String arg1, String arg2)
            throws SAXException {
        super.endElement(usedNamespaceUri, arg1, arg2);
    }

    @Override
    public void startPrefixMapping(String prefix, String url)
            throws SAXException {
        if (addNamespace) {
            startControlledPrefixMapping();
        }
    }

    private void startControlledPrefixMapping()
            throws SAXException {
        if (addNamespace && !addedNamespace) {
            // We should add namespace since it is set and has not yet been done.
            super.startPrefixMapping("", usedNamespaceUri);

            // Make sure we dont do it twice
            addedNamespace = true;
        }
    }
}
