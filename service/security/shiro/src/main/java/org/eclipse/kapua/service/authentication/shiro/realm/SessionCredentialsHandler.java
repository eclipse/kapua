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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.eclipse.kapua.service.authentication.SessionCredentials;

/**
 * {@link SessionCredentials} {@link CredentialsHandler} definition.
 * <p>
 * It maps a {@link SessionCredentials} to a specific implementation.
 *
 * @since 2.0.0
 */
public interface SessionCredentialsHandler extends CredentialsHandler<SessionCredentials> {

}
