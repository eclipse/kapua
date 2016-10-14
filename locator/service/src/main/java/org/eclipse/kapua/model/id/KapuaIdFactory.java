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
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Kapua identifier factory definition.
 * 
 * @since 1.0
 *
 */
public interface KapuaIdFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaId} starting the provided short identifier.<br>
     * <b>This operation must be the inverse function of {@link KapuaId#getShortId} so, in other word, this code should't fail:
     * </b>
     * 
     * <pre>
     * {@code
     * KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getService("some KapuaIdFactory implementation");<br>
     * String shortId = "some well formed encoded short id";
     * KapuaId id = kapuaIdFactory.newKapuaId(shortId);
     * String shortIdConverted = id.getShortId();
     * AssertTrue(shortId.equals(shortIdConverted));
     * }
     * </pre>
     * 
     * @param shortId
     * @return
     */
	public KapuaId newKapuaId(String shortId);

}
