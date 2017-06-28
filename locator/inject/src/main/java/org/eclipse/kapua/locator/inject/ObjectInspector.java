/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.locator.inject;

import java.lang.reflect.Method;

public interface ObjectInspector {

    public boolean isEnhancedClass(Object obj);
    public Method[] getSuperMethods(Object obj);
    public Method[] getMethods(Object obj);
}
