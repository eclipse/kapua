/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception.model;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.job.engine.exception.CleanJobDataException;
import org.eclipse.kapua.job.engine.exception.KapuaJobEngineErrorCodes;

@XmlRootElement(name = "cleanJobDataExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CleanJobDataExceptionInfo extends JobEngineExceptionInfo {

    public CleanJobDataExceptionInfo() {
        this(null);
    }

    public CleanJobDataExceptionInfo(CleanJobDataException cleanJobDataException) {
        super(Status.INTERNAL_SERVER_ERROR, KapuaJobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA, cleanJobDataException);
    }

}
