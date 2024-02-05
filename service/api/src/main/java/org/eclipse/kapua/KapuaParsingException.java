/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

public class KapuaParsingException extends KapuaException {

    private static final long serialVersionUID = -7067191269730263113L;

    public KapuaParsingException(String thingToParse) {
        super(KapuaErrorCodes.PARSING_ERROR, thingToParse);
    }

}
