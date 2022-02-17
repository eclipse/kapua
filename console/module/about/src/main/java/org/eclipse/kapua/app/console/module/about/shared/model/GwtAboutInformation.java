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
