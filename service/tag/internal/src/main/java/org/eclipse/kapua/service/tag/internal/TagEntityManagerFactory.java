/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import javax.inject.Singleton;

/**
 * {@link TagServiceImpl} {@link EntityManagerFactory} implementation.
 *
 * @since 1.0.0
 * @deprecated since 2.0.0, use {@link org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory} instead
 */
@Singleton
@Deprecated
public class TagEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-tag";

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public TagEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME);
    }
}
