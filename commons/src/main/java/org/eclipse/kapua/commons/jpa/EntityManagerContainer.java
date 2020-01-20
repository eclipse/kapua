/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

public class EntityManagerContainer<T> {

    OnAfter<T> onAfter;
    OnBefore<T> onBefore;
    EntityManagerCallback<T> resultCallback;

    public static <T> EntityManagerContainer<T> create() {
        return new EntityManagerContainer<>();
    }

    private EntityManagerContainer() {
    }

    public EntityManagerContainer<T> onAfterHandler(OnAfter<T> onAfter) {
        this.onAfter = onAfter;
        return this;
    }

    public EntityManagerContainer<T> onBeforeHandler(OnBefore<T> onBefore) {
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
}
