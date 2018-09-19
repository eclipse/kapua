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
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.model.id.KapuaId;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Base64;

/**
 * Kapua identifier reference implementation.
 *
 * @since 1.0
 */
@Embeddable
public class KapuaEid implements KapuaId, Serializable {

    private static final long serialVersionUID = 8998805462408705432L;

    protected BigInteger eid;

    /**
     * Constructor
     */
    public KapuaEid() {
        super();
    }

    /**
     * Constructor
     *
     * @param id
     */
    public KapuaEid(BigInteger id) {
        this();
        setId(id);
    }

    /**
     * Constructor
     *
     * @param id
     */
    public KapuaEid(KapuaId id) {
        this();
        setId(id.getId());
    }

    public static KapuaEid parseKapuaId(KapuaId kapuaId) {
        return kapuaId != null ? (kapuaId instanceof KapuaEid ? (KapuaEid) kapuaId : new KapuaEid(kapuaId)) : null;
    }

    /**
     * Creates a {@link KapuaEid} instance parsing the short identifier provided
     *
     * @param shortId
     * @return
     */
    public static KapuaEid parseCompactId(String shortId) {
        byte[] bytes = Base64.getUrlDecoder().decode(shortId);
        return new KapuaEid(new BigInteger(bytes));
    }

    @Override
    public BigInteger getId() {
        return eid;
    }

    /**
     * Set the identifier
     *
     * @param eid
     */
    protected void setId(BigInteger eid) {
        this.eid = eid;
    }

    @Override
    public String toString() {
        return eid.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (eid == null ? 0 : eid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof KapuaId)) {
            return false;
        }
        KapuaId other = (KapuaId) obj;
        if (eid == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!eid.equals(other.getId())) {
            return false;
        }
        return true;
    }
}
