/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.job.client.targets;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JobTargetRestartTargetDialog extends SimpleDialog {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private static final GwtJobEngineServiceAsync JOB_ENGINE_SERVICE = GWT.create(GwtJobEngineService.class);

    private final GwtJob gwtJob;
    private final GwtJobTarget gwtJobTarget;

    public JobTargetRestartTargetDialog(GwtJob gwtJob, GwtJobTarget gwtJobTarget) {
        this.gwtJob = gwtJob;
        this.gwtJobTarget = gwtJobTarget;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void createBody() {
        // No action needed
    }

    @Override
    protected void addListeners() {
        // No action needed
    }

    @Override
    public void submit() {
        JOB_ENGINE_SERVICE.restart(gwtJob.getScopeId(), gwtJob.getId(), gwtJobTarget.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.popupError(), JOB_MSGS.jobStartTargetErrorMessage(caught.getLocalizedMessage()));
                unmask();
                hide();
            }

            @Override
            public void onSuccess(Void result) {
                ConsoleInfo.display(MSGS.popupInfo(), JOB_MSGS.jobStartTargetStartedMessage());
                unmask();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.jobStartTargetDialogHeader(gwtJob.getJobName(), gwtJobTarget.getClientId());
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return new KapuaIcon(IconSet.WARNING);
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.jobStartTargetDialogInfo();
    }
}
