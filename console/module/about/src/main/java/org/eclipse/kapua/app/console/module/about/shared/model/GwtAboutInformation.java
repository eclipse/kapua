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
package org.eclipse.kapua.app.console.module.about.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GwtAboutInformation implements Serializable {

    private static final long serialVersionUID = -1399325955482672179L;

    private List<GwtAboutDependency> dependencies = new ArrayList<GwtAboutDependency>();

    public void setDependencies(List<GwtAboutDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public List<GwtAboutDependency> getDependencies() {
        return dependencies;
    }
}
