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
package org.eclipse.kapua.app.console.module.api.client.util;

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
