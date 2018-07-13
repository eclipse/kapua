/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

/**
 * A object context acts as a factory and provides object instances 
 * from classes as defined in {@link ObjectContextConfig}.
 */
public interface ObjectContext {

    public <T> T getInstance(Class<T> clazz);
}
