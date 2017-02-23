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
package org.eclipse.kapua.model.id;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Base64;

/**
 * Kapua identifier object.<br>
 * This object it's used to identify each entity.<br>
 * <b>It should be unique within the same entity.</b>
 * 
 * @since 1.0
 *
 */
public interface KapuaId extends Serializable
{
    /**
     * Get the identifier
     * 
     * @return
     */
    public BigInteger getId();

    /**
     * Get the identifier (Base64 url encoded identifier version)
     * 
     * @return
     */
    default public String toCompactId()
    {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(getId().toByteArray());
    }
}
