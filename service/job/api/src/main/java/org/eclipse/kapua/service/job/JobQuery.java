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
package org.eclipse.kapua.service.job;

import org.eclipse.kapua.model.query.KapuaQuery;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Job} {@link KapuaQuery} definition.
 *
 * @see KapuaQuery
 * @since 1.0.0
 */
@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobXmlRegistry.class, factoryMethod = "newQuery")
public interface JobQuery extends KapuaQuery {

    /**
     * Instantiates a new {@link JobMatchPredicate}.
     *
     * @param matchTerm The term to use to match.
     * @param <T>       The type of the term
     * @return The newly instantiated {@link JobMatchPredicate}.
     * @since 2.0.0
     */
    <T> JobMatchPredicate<T> matchPredicate(T matchTerm);

}
