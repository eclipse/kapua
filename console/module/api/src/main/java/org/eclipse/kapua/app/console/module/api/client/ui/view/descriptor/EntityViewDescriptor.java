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
package org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

public interface EntityViewDescriptor<M extends GwtEntityModel> extends ViewDescriptor, IsSerializable {

    EntityView<M> getViewInstance(GwtSession currentSession);
}
