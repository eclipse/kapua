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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

public class TagEditDialog extends TagAddDialog {

    private final static GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    private final static ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);
    private GwtTag selectedTag;

    public TagEditDialog(GwtSession currentSession, GwtTag selectedTag) {
        super(currentSession);
        this.selectedTag = selectedTag;
    }

    @Override
    public void createBody() {

        super.createBody();
        populateEditDialog(selectedTag);
    }

    @Override
    public void submit() {
        selectedTag.setTagName(tagNameField.getValue());
        GWT_TAG_SERVICE.update(selectedTag, new AsyncCallback<GwtTag>() {

            @Override
            public void onFailure(Throwable arg0) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(arg0.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(GwtTag arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedTag.getTagName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtTag gwtTag) {
        tagNameField.setValue(gwtTag.getTagName());

    }

}
