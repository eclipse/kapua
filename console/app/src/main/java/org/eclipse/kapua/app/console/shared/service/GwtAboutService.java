/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.shared.model.GwtAboutDependency;
import org.eclipse.kapua.app.console.shared.model.GwtAboutInformation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This service retrieves a subset of configuration values for front-end usage.
 */
@RemoteServiceRelativePath("about")
public interface GwtAboutService extends RemoteService {

    public GwtAboutInformation getInformation();

    public GwtAboutDependency dummyGwtAboutDependency();
}