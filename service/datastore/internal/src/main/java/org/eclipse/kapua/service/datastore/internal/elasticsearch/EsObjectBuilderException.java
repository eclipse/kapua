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
 * Elasticsearch object builder exception.<br>
 * This exception is raised during conversion from Elasticsearch to Kapua objects
 * 
 * @since 1.0
 *
 */
public class EsObjectBuilderException extends EsDatastoreException
{
    private static final long serialVersionUID = 8585332105235907551L;

    /**
     * Construct the exception with the provided message
     * 
     * @param message
     */
    public EsObjectBuilderException(String message)
    {
        super(message);
    }

    /**
     * Construct the exception with the provided throwable
     * 
     * @param t
     */
    public EsObjectBuilderException(Throwable t)
    {
        super(t);
    }

    /**
     * Construct the exception with the provided reason and throwable
     * 
     * @param reason
     * @param t
     */
    public EsObjectBuilderException(String reason, Throwable t)
    {
        super(reason, t);
    }
}
