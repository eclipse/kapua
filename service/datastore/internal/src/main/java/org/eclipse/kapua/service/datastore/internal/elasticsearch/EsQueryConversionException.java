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
 * Query conversion exception.<br>
 * This exception is raised if something fails during conversion between Kapua and Elasticsearch query object
 * 
 * @since 1.0
 *
 */
public class EsQueryConversionException extends EsDatastoreException
{

    private static final long serialVersionUID = 8935946838486350152L;

    /**
     * Construct the exception with the provided message
     * 
     * @param message
     */
    public EsQueryConversionException(String message)
    {
        super(message);
    }

    /**
     * Construct the exception with the provided exception
     * 
     * @param e
     */
    public EsQueryConversionException(Exception e)
    {
        super(e);
    }
}
