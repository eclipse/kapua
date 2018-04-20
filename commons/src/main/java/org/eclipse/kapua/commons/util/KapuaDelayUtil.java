/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
