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
package org.eclipse.kapua.app.console.module.about.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutDependency;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutInformation;

/**
 * This service retrieves a subset of configuration values for front-end usage.
 */
@RemoteServiceRelativePath("about")
public interface GwtAboutService extends RemoteService {

    public GwtAboutInformation getInformation();

    public GwtAboutDependency dummyGwtAboutDependency();
}
