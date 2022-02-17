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
package org.eclipse.kapua.app.console.core.client.util;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public class ScaledAbstractImagePrototype extends AbstractImagePrototype {
    private AbstractImagePrototype aip;

    public ScaledAbstractImagePrototype(AbstractImagePrototype aip) {
        this.aip = aip;
    }

    @Override
    public void applyTo(Image image) {
        aip.applyTo(image);
    }

    @Override
    public Image createImage() {
        Image img = aip.createImage();
        return new Image(img.getUrl());
    }

    @Override
    public ImagePrototypeElement createElement() {
        ImagePrototypeElement imgElement = aip.createElement();
        imgElement.getStyle().setProperty("backgroundSize", "100%");
        return imgElement;
    }
}
