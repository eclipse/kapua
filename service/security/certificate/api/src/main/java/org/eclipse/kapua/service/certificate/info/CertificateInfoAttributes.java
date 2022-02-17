/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.info;

import org.eclipse.kapua.model.KapuaNamedEntityAttributes;

/**
 * {@link CertificateInfoAttributes} attributes.
 *
 * @see org.eclipse.kapua.model.KapuaEntityAttributes
 * @since 1.1.0
 */
public class CertificateInfoAttributes extends KapuaNamedEntityAttributes {

    public static final String CA_ID = "caId";

    public static final String FORWARDABLE = "forwardable";

    public static final String SIGNATURE = "signature";

    public static final String STATUS = "status";

    public static final String USAGE = "certificateUsages";

    public static final String USAGE_NAME = USAGE + ".name";

}
