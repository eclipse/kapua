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
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.util.Set;

/**
 * @deprecated since 2.0.0 - use dependency injection to fetch ServiceModules where needed, there is no need to introduce additional indirection
 */
@Deprecated
public interface ServiceModuleProvider {

    Set<ServiceModule> getModules();
}
