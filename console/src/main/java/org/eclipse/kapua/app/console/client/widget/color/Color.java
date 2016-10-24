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
package org.eclipse.kapua.app.console.client.widget.color;

public enum Color {
    /**
     * Dark blue used in Kapua logo
     */
    BLUE_KAPUA(16, 38, 68),

    /**
     * Shade of cyan used in Kapua logo
     */
    CYAN_KAPUA(64, 139, 211),

    /**
     * Regular white
     */
    WHITE(255, 255, 255),

    /**
     * Nice green
     */
    GREEN(29, 158, 116),

    /**
     * Regular yellow
     */
    YELLOW(230, 200, 29);

    int r;
    int g;
    int b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
