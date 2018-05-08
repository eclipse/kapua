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
package org.eclipse.kapua.app.console.module.tag.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

public class TagDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);

    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);

    private GwtTag gwtTag;

    public TagDeleteDialog(GwtTag gwtTag) {
        this.gwtTag = gwtTag;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        GWT_TAG_SERVICE.delete(gwtTag.getScopeId(), gwtTag.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable arg0) {
                FailureHandler.handle(arg0);
                exitStatus = false;
                exitMessage = MSGS.dialogDeleteError(arg0.getLocalizedMessage());
                hide();

            }

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogDeleteConfirmation();
                hide();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader(gwtTag.getTagName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

}
