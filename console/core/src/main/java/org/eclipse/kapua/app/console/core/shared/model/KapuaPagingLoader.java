/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
