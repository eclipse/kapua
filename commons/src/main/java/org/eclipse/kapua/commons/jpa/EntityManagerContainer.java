/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

public class EntityManagerContainer<T> {

    private OnAfterResult<T> onAfter;
    private OnBeforeResult<T> onBefore;
    private EntityManagerCallback<T> resultCallback;

    public static <T> EntityManagerContainer<T> create() {
        return new EntityManagerContainer<>();
    }

    private EntityManagerContainer() {
    }

    public EntityManagerContainer<T> onAfterHandler(OnAfterResult<T> onAfter) {
        this.onAfter= onAfter;
        return this;
    }

    public EntityManagerContainer<T> onBeforeHandler(OnBeforeResult<T> onBefore) {
        this.onBefore = onBefore;
        return this;
    }

    public EntityManagerContainer<T> onResultHandler(EntityManagerCallback<T> resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    public T onResult(EntityManager entityManager) throws KapuaException {
        return resultCallback.onAction(entityManager);
    }

    public T onBefore() throws KapuaException {
        if (onBefore != null) {
            return onBefore.onBefore();
        }
        else {
            return null;
        }
    }

    public void onAfter(T instance) throws KapuaException {
        if (onAfter != null) {
            onAfter.onAfter(instance);
        }
    }
}
