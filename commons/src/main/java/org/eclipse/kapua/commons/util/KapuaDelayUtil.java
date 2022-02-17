/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

public class KapuaDelayUtil {

    public static void executeDelay(){
            try {
                Thread.sleep((long) (Math.random()*1000));
            } catch (InterruptedException e) {
                //Restore the interupted status
                Thread.currentThread().interrupt();
            }
        }

    private KapuaDelayUtil() {
        super();
    }
}
