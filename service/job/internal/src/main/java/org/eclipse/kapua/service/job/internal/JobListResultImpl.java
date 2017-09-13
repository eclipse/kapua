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
