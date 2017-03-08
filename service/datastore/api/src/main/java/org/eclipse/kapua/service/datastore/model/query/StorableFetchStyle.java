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
package org.eclipse.kapua.service.datastore.model.query;

/**
 * Fetch style behavior
 * 
 * @since 1.0
 *
 */
public enum StorableFetchStyle
{
    /**
     * Only indexed fields
     */
    FIELDS,
    /**
     * Full document (except the message body)
     */
    SOURCE_SELECT,
    /**
     * Full document
     */
    SOURCE_FULL;
}
