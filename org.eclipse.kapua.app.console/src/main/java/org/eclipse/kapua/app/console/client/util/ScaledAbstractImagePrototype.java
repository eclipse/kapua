/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.util;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public class ScaledAbstractImagePrototype extends AbstractImagePrototype {
    private AbstractImagePrototype m_aip;

    public ScaledAbstractImagePrototype(AbstractImagePrototype aip) {
        m_aip = aip;
    }

    @Override
    public void applyTo(Image image) {
        m_aip.applyTo(image);
    }

    @Override
    public Image createImage() {
        Image img = m_aip.createImage();
        return new Image(img.getUrl());
    }

    @Override
    public ImagePrototypeElement createElement() {
        ImagePrototypeElement imgElement = m_aip.createElement();
        imgElement.getStyle().setProperty("backgroundSize", "100%");
        return imgElement;
    }
}
