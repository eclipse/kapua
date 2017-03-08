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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

/**
 * Elasticsearch docuemnt builder exception.<br>
 * This exception is raised during conversion from Kapua to Elasticsearch objects
 * 
 * @since 1.0
 *
 */
public class EsDocumentBuilderException extends EsDatastoreException
{

    private static final long serialVersionUID = -978823971375607146L;

    /**
     * Construct the exception with the provided message
     * 
     * @param message
     */
    public EsDocumentBuilderException(String message)
    {
        super(message);
    }

    /**
     * Construct the exception with the provided throwable
     * 
     * @param t
     */
    public EsDocumentBuilderException(Throwable t)
    {
        super(t);
    }

    /**
     * Construct the exception with the provided reason and throwable
     * 
     * @param reason
     * @param t
     */
    public EsDocumentBuilderException(String reason, Throwable t)
    {
        super(reason, t);
    }
}
