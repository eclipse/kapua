/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.util;

import java.util.Date;

public final class Years {

    private Years() {
    }

    /**
     * Get the current year in a GWT compatible manner
     * 
     * @return the current, 4-digit year
     */
    public static int getCurrentYear() {
        Date now = new Date();
        @SuppressWarnings("deprecation")
        int year = now.getYear() + 1900;
        return year;
    }

}
