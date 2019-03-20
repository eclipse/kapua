/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.message.KapuaChannel;

import java.util.List;

/**
 * {@link KapuaChannel} implementation.
 *
 * @since 1.0.0
 */
public class KapuaChannelImpl implements KapuaChannel {

    private List<String> semanticParts;

    @Override
    public List<String> getSemanticParts() {
        return semanticParts;
    }

    @Override
    public void setSemanticParts(final List<String> semanticParts) {
        this.semanticParts = semanticParts;
    }

    @Override
    public String toString() {
        return semanticParts != null ? String.join("/", semanticParts) : "";
    }

}
