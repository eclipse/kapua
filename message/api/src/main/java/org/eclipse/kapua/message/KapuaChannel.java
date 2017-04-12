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
package org.eclipse.kapua.message;

import java.util.List;

/**
 * Kapua message channel object definition.
 *
 * @since 1.0
 */
public interface KapuaChannel extends Channel {

    /**
     * Get the channel destination semantic part
     *
     * @return
     */
    public List<String> getSemanticParts();

    /**
     * Set the channel destination semantic part
     *
     * @param semanticParts
     */
    public void setSemanticParts(List<String> semanticParts);
}
