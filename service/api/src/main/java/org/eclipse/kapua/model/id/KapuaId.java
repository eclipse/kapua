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
package org.eclipse.kapua.model.id;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Base64;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Kapua identifier object.<br>
 * This object it's used to identify each entity.<br>
 * <b>It should be unique within the same entity.</b>
 * 
 * @since 1.0.0
 *
 */
public interface KapuaId extends Serializable {

    @XmlTransient
    public static final KapuaId ANY = new KapuaIdStatic(BigInteger.ONE.negate());

    /**
     * Get the identifier
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public BigInteger getId();

    /**
     * Get the identifier Base64 URL encoded formatted.
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public default String toCompactId() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(getId().toByteArray());
    }

    /**
     * Get the identifier numeric String formatted.
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public default String toStringId() {
        return getId().toString();
    }

}
