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

import java.io.Serializable;

public class GwtLoginInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String backgroundCredits;

    private String informationSnippet;

    public String getBackgroundCredits() {
        return backgroundCredits;
    }

    public void setBackgroundCredits(String backgroundCredits) {
        this.backgroundCredits = backgroundCredits;
    }

    public String getInformationSnippet() {
        return informationSnippet;
    }

    public void setInformationSnippet(String informationSnippet) {
        this.informationSnippet = informationSnippet;
    }
}
