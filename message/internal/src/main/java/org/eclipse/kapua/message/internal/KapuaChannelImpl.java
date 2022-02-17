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
