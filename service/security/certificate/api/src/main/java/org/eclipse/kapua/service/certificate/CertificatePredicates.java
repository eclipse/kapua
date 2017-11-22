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
package org.eclipse.kapua.service.certificate;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

public interface CertificatePredicates extends KapuaUpdatableEntityPredicates {

    /**
     * Certificate name
     */
    public static final String NAME = "name";

    public static final String USAGE = "certificateUsages";

    public static final String USAGE_NAME = USAGE + ".name";

}
