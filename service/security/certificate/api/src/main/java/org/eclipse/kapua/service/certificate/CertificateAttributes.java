/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaNamedEntityAttributes;

public class CertificateAttributes extends KapuaNamedEntityAttributes {

    public static final String CA_ID = "caId";

    public static final String FORWARDABLE = "forwardable";

    public static final String SIGNATURE = "signature";

    public static final String STATUS = "status";

    public static final String USAGE = "certificateUsages";

    public static final String USAGE_NAME = USAGE + ".name";

}
