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

public class GwtProductInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String backgroundCredits;

    private String informationSnippet;

    private String productName;

    private String copyright;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
