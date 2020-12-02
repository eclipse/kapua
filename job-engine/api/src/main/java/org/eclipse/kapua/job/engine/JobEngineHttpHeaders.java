/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine;

public interface JobEngineHttpHeaders {

    String SCOPE_ID_HTTP_HEADER = "X-Kapua-Scope-ID";
    String USER_ID_HTTP_HEADER = "X-Kapua-User-ID";
    String AUTH_MODE = "X-Kapua-Auth-Mode";
}
