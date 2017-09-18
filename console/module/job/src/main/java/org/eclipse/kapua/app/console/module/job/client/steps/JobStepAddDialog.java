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
package org.eclipse.kapua.app.console.module.job.client.steps;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepDefinition;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepDefinitionQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepDefinitionService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepDefinitionServiceAsync;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobStepAddDialog extends EntityAddEditDialog {

    private final String jobId;
    protected int jobStepIndex;

    protected final TextField<String> jobStepName;
    protected final TextField<String> jobStepDescription;
    protected final ComboBox<GwtJobStepDefinition> jobStepDefinitionCombo;
    protected final FieldSet jobStepPropertiesFieldSet;
    protected final Label jobStepDefinitionDescription;
    protected final FormPanel jobStepPropertiesPanel;

    protected static final String PROPERTY_NAME = "propertyName";
    protected static final String PROPERTY_TYPE = "propertyType";

    protected static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobStepDefinitionServiceAsync JOB_STEP_DEFINITION_SERVICE = GWT.create(GwtJobStepDefinitionService.class);
    private static final GwtJobStepServiceAsync JOB_STEP_SERVICE = GWT.create(GwtJobStepService.class);

    public JobStepAddDialog(GwtSession currentSession, String jobId) {
        super(currentSession);

        this.jobId = jobId;

        jobStepName = new TextField<String>();
        jobStepDescription = new TextField<String>();
        jobStepDefinitionCombo = new ComboBox<GwtJobStepDefinition>();
        jobStepPropertiesFieldSet = new FieldSet();
        jobStepDefinitionDescription = new Label();
        jobStepPropertiesPanel = new FormPanel(FORM_LABEL_WIDTH);

        DialogUtils.resizeDialog(this, 600, 400);
    }

    @Override
    public void createBody() {
        FormPanel jobStepFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        jobStepName.setFieldLabel(JOB_MSGS.dialogAddStepJobNameLabel());
        jobStepName.setAllowBlank(false);

        jobStepDescription.setFieldLabel(JOB_MSGS.dialogAddStepJobDescriptionLabel());

        GwtJobStepDefinitionQuery query = new GwtJobStepDefinitionQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        RpcProxy<ListLoadResult<GwtJobStepDefinition>> jobStepDefinitionProxy = new RpcProxy<ListLoadResult<GwtJobStepDefinition>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtJobStepDefinition>> callback) {
                JOB_STEP_DEFINITION_SERVICE.findAll(callback);
            }
        };

        BaseListLoader<ListLoadResult<GwtJobStepDefinition>> jobStepDefinitionLoader = new BaseListLoader<ListLoadResult<GwtJobStepDefinition>>(jobStepDefinitionProxy);
        ListStore<GwtJobStepDefinition> jobStepDefinitionStore = new ListStore<GwtJobStepDefinition>(jobStepDefinitionLoader);
        jobStepDefinitionCombo.setStore(jobStepDefinitionStore);
        jobStepDefinitionCombo.setDisplayField("jobStepDefinitionName");
        jobStepDefinitionCombo.setFieldLabel(JOB_MSGS.dialogAddStepDefinitionCombo());
        jobStepDefinitionCombo.setEmptyText(JOB_MSGS.dialogAddStepDefinitionComboEmpty());
        jobStepDefinitionCombo.setEditable(false);
        jobStepDefinitionCombo.setAllowBlank(false);
        jobStepDefinitionCombo.setForceSelection(true);
        jobStepDefinitionCombo.setTypeAhead(false);
        jobStepDefinitionCombo.setTriggerAction(TriggerAction.ALL);
        jobStepDefinitionCombo.addSelectionChangedListener(new SelectionChangedListener<GwtJobStepDefinition>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtJobStepDefinition> selectionChangedEvent) {
                refreshJobStepDefinition(selectionChangedEvent.getSelectedItem());
            }
        });

        jobStepPropertiesFieldSet.setHeading(JOB_MSGS.jobStepPropertiesFieldSetHeading());
        jobStepPropertiesFieldSet.setVisible(false);

        jobStepDefinitionDescription.setStyleAttribute("display", "block");

        jobStepPropertiesFieldSet.add(jobStepDefinitionDescription);
        jobStepPropertiesFieldSet.add(jobStepPropertiesPanel);

        jobStepFormPanel.add(jobStepName);
        jobStepFormPanel.add(jobStepDescription);
        jobStepFormPanel.add(jobStepDefinitionCombo);
        jobStepFormPanel.add(jobStepPropertiesFieldSet);

        bodyPanel.add(jobStepFormPanel);
    }

    @Override
    public void submit() {
        GwtJobStepCreator gwtJobStepCreator = new GwtJobStepCreator();

        gwtJobStepCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtJobStepCreator.setJobName(jobStepName.getValue());
        gwtJobStepCreator.setJobDescription(jobStepDescription.getValue());
        gwtJobStepCreator.setJobId(jobId);
        gwtJobStepCreator.setJobStepDefinitionId(jobStepDefinitionCombo.getValue().getId());
        gwtJobStepCreator.setStepIndex(jobStepIndex);
        gwtJobStepCreator.setProperties(readStepProperties());

        JOB_STEP_SERVICE.create(xsrfToken, gwtJobStepCreator, new AsyncCallback<GwtJobStep>() {

            @Override
            public void onSuccess(GwtJobStep arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogAddStepConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = JOB_MSGS.dialogAddStepError(cause.getLocalizedMessage());

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogAddStepHeader();
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogAddStepInfo();
    }

    private void refreshJobStepDefinition(GwtJobStepDefinition gwtJobStepDefinition) {
        jobStepPropertiesFieldSet.setVisible(true);
        jobStepDefinitionDescription.setText(gwtJobStepDefinition.getDescription());
        jobStepPropertiesPanel.removeAll();

        for (GwtJobStepProperty property : gwtJobStepDefinition.getStepProperties()) {
            String propertyType = property.getPropertyType();
            if (
                    propertyType.equals(String.class.getName()) ||
                            propertyType.equals(Long.class.getName()) ||
                            propertyType.equals(Integer.class.getName()) ||
                            propertyType.equals(Float.class.getName()) ||
                            propertyType.equals(Double.class.getName())
                    ) {
                TextField<String> textField = new TextField<String>();
                textField.setFieldLabel(property.getPropertyName());
                textField.setEmptyText(KapuaSafeHtmlUtils.htmlUnescape(property.getPropertyValue()));
                textField.setData(PROPERTY_TYPE, property.getPropertyType());
                textField.setData(PROPERTY_NAME, property.getPropertyName());
                jobStepPropertiesPanel.add(textField);
            } else if (propertyType.equals(Boolean.class.getName())) {
                CheckBox checkBox = new CheckBox();
                checkBox.setFieldLabel(property.getPropertyName());
                checkBox.setValue(Boolean.valueOf(property.getPropertyValue()));
                checkBox.setData(PROPERTY_TYPE, property.getPropertyType());
                checkBox.setData(PROPERTY_NAME, property.getPropertyName());
                jobStepPropertiesPanel.add(checkBox);
            } else {
                TextArea textArea = new TextArea();
                textArea.setFieldLabel(property.getPropertyName());
                textArea.setEmptyText(KapuaSafeHtmlUtils.htmlUnescape(property.getPropertyValue()));
                textArea.setData(PROPERTY_TYPE, property.getPropertyType());
                textArea.setData(PROPERTY_NAME, property.getPropertyName());
                jobStepPropertiesPanel.add(textArea);
            }
        }

        jobStepPropertiesPanel.layout(true);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        JOB_STEP_SERVICE.getFirstFreeStepIndex(currentSession.getSelectedAccountId(), jobId, new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Integer result) {
                jobStepIndex = result;
            }
        });
    }

    protected List<GwtJobStepProperty> readStepProperties() {
        List<GwtJobStepProperty> jobStepProperties = new ArrayList<GwtJobStepProperty>();
        for (Component component : jobStepPropertiesPanel.getItems()) {
            Field field = (Field) component;
            GwtJobStepProperty property = new GwtJobStepProperty();
            property.setPropertyValue(!field.getRawValue().isEmpty() ? field.getRawValue() : null);
            property.setPropertyType(field.getData(PROPERTY_TYPE).toString());
            property.setPropertyName(field.getData(PROPERTY_NAME).toString());
            jobStepProperties.add(property);
        }
        return jobStepProperties;
    }
}
