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
 * Invalid channel exception.<br>
 * This exceptinis raised if the specified channel is not compliant with the Kapua rules.
 * 
 * @since 1.0
 *
 */
public class EsInvalidChannelException extends EsDatastoreException
{

    private static final long serialVersionUID = 2841854292521738239L;

    /**
     * Construct the exception with the provided channel string representation
     * 
     * @param channel
     */
    public EsInvalidChannelException(String channel)
    {
        super(channel);
    }
}
