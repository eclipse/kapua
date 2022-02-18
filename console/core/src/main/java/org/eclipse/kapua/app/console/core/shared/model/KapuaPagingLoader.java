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
package org.eclipse.kapua.app.console.core.shared.model;

import com.extjs.gxt.ui.client.data.PagingLoader;

public interface KapuaPagingLoader<D extends KapuaPagingLoadResult<?>> extends PagingLoader<D> {

    public int getVirtualOffset();

    public void setVirtualOffset(int offset);
}
