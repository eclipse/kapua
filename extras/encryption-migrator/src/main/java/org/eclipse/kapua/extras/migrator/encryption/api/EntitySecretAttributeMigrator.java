/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.api;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;

import java.util.List;

public interface EntitySecretAttributeMigrator<E extends KapuaEntity> {

    String getEntityName();

    List<E> getChunk(int offset, int limit) throws KapuaException;

    long getTotalCount() throws KapuaException;

    void migrate(List<E> entitiesToMigrate) throws KapuaException;
}
