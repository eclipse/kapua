/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.client.steps;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepDefinition;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepDefinitionService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepDefinitionServiceAsync;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepServiceAsync;

import java.util.HashMap;
import java.util.Map;

public class JobStepEditDialog extends JobStepAddDialog {

    private GwtJobStep selectedJobStep;
    private static final GwtJobStepDefinitionServiceAsync JOB_STEP_DEFINITION_SERVICE = GWT.create(GwtJobStepDefinitionService.class);
    private static final GwtJobStepServiceAsync JOB_STEP_SERVICE = GWT.create(GwtJobStepService.class);

    public JobStepEditDialog(GwtSession currentSession, GwtJobStep selectedJobStep) {
        super(currentSession, selectedJobStep.getJobId());
        this.selectedJobStep = selectedJobStep;
    }

    @Override
    public void createBody() {
        super.createBody();

        loadJobStep();
    }

    private void loadJobStep() {
        maskDialog();
        jobStepDefinitionCombo.getStore().getLoader().addLoadListener(new StepDefinitionLoadListener());
        jobStepDefinitionCombo.getStore().getLoader().load();
    }

    public void populateEditDialog(final GwtJobStep gwtJobStep) {
        JOB_STEP_DEFINITION_SERVICE.find(gwtJobStep.getJobStepDefinitionId(), new AsyncCallback<GwtJobStepDefinition>() {

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogEditError(caught.getLocalizedMessage());

                hide();
            }

            @Override
            public void onSuccess(GwtJobStepDefinition result) {
                jobStepName.setValue(gwtJobStep.getUnescapedJobStepName());
                jobStepDescription.setValue(gwtJobStep.getUnescapedDescription());
                jobStepDefinitionCombo.setValue(result);

                Map<String, Object> propertiesMap = new HashMap<String, Object>();
                for (GwtJobStepProperty property : gwtJobStep.getStepProperties()) {
                    if (property.getPropertyType().equals(Long.class.getName())) {
                        propertiesMap.put(property.getPropertyName(),
                                property.getPropertyValue() == null ? null : Long.valueOf(property.getPropertyValue()));
                    } else {
                        propertiesMap.put(property.getPropertyName(), property.getPropertyValue());
                    }
                }

                for (Component component : jobStepPropertiesPanel.getItems()) {
                    Field<Object> field = (Field<Object>) component;
                    field.setValue(propertiesMap.get(field.getData(PROPERTY_NAME)));
                }

                unmaskDialog();
            }
        });
    }

    @Override
    public void submit() {
        selectedJobStep.setJobStepName(KapuaSafeHtmlUtils.htmlUnescape(jobStepName.getValue()));
        selectedJobStep.setDescription(KapuaSafeHtmlUtils.htmlUnescape(jobStepDescription.getValue()));
        selectedJobStep.setJobStepDefinitionId(jobStepDefinitionCombo.getValue().getId());
        selectedJobStep.setStepProperties(readStepProperties());

        JOB_STEP_SERVICE.update(xsrfToken, selectedJobStep, new AsyncCallback<GwtJobStep>() {

            @Override
            public void onSuccess(GwtJobStep arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogEditStepConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (cause instanceof GwtKapuaException) {
                    GwtKapuaException gwtCause = (GwtKapuaException) cause;
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                        jobStepName.markInvalid(gwtCause.getMessage());
                    }
                }
            }
        });

    }

    public class StepDefinitionLoadListener extends LoadListener {

        @Override
        public void loaderLoad(LoadEvent le) {
            super.loaderLoad(le);
            populateEditDialog(selectedJobStep);
        }
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogEditStepHeader();
    }

}
