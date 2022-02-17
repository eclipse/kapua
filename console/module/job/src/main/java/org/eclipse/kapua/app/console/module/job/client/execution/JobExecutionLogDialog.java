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
package org.eclipse.kapua.app.console.module.job.client.execution;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityLogDialog;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecution;

public class JobExecutionLogDialog extends EntityLogDialog {

    private static final ConsoleJobMessages MSG_JOB = GWT.create(ConsoleJobMessages.class);

    public JobExecutionLogDialog(GwtJobExecution jobExecution) {
        super(MSG_JOB.jobExecutionLogDialogHeader(), jobExecution.getUnescapedLog());
    }
}
