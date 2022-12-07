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
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.model.id.KapuaId;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Base64;

/**
 * {@link KapuaId} implementation.
 *
 * @since 1.0.0
 */
@Embeddable
public class KapuaEid implements KapuaId, Serializable {

    private static final long serialVersionUID = 8998805462408705432L;

    protected final BigInteger eid;

    /**
     * Constructor.
     * <p>
     * Note: this make any sense? A {@link KapuaId} whose {@link KapuaId#getId()} is {@code null}?
     *
     * @since 1.0.0
     */
    public KapuaEid() {
        super();

        eid = null;
    }

    /**
     * Constructor.
     *
     * @param id The id in {@link BigInteger} form.
     * @since 1.0.0
     */
    public KapuaEid(BigInteger id) {
        eid = id;
    }

    /**
     * Constructor.
     *
     * @param id The id in {@link KapuaId} form.
     * @since 1.0.0
     */
    public KapuaEid(KapuaId id) {
        eid = id.getId();
    }

    /**
     * Parses the given {@link KapuaId} to a {@link KapuaEid} form.
     *
     * @param kapuaId The {@link KapuaId} to parse.
     * @return The parsed {@link KapuaId} as {@link KapuaEid}.
     * @since 1.0.0
     */
    public static KapuaEid parseKapuaId(KapuaId kapuaId) {
        return kapuaId != null ? (kapuaId instanceof KapuaEid ? (KapuaEid) kapuaId : new KapuaEid(kapuaId)) : null;
    }

    /**
     * Parses the given {@link KapuaId} in short form.
     *
     * @param shortId The {@link KapuaId} in short form to parse.
     * @return The parsed {@link KapuaId}.
     * @since 1.0.0
     */
    public static KapuaEid parseCompactId(String shortId) {
        byte[] bytes = Base64.getUrlDecoder().decode(shortId);
        return new KapuaEid(new BigInteger(bytes));
    }

    @Override
    public BigInteger getId() {
        return eid;
    }

    @Override
    public String toString() {
        return KapuaId.toString(this);
    }

    @Override
    public int hashCode() {
        return KapuaId.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return KapuaId.areEquals(this, obj);
    }
}
