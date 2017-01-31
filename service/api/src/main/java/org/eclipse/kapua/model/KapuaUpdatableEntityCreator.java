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
package org.eclipse.kapua.model;

/**
 * {@link KapuaUpdatableEntityCreator} definition.<br>
 * All the {@link KapuaUpdatableEntityCreator}s will be an extension of this entity.<br>
 *
 * This class extends {@link KapuaEntityCreator} and adds further properties to updatable creator model objects in Kapua.
 *
 * @param <E>
 *            The {@link KapuaEntity} of which this {@link KapuaUpdatableEntityCreator} is the creator model.
 * 
 * @since 1.0.0
 */
public interface KapuaUpdatableEntityCreator<E extends KapuaEntity> extends KapuaEntityCreator<E> {

}
