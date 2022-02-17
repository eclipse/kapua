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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;

/**
 * {@link JobListResult} implementation.
 *
 * @since 1.0.0
 */
public class JobListResultImpl extends KapuaListResultImpl<Job> implements JobListResult {

    private static final long serialVersionUID = -7466853155307881848L;

}
