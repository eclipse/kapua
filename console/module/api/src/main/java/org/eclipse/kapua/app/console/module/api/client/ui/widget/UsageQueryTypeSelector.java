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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

public class UsageQueryTypeSelector extends LayoutContainer {

    public enum QueryType {
        DAY, HOUR
    }

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private SimpleComboBox<String> queryType;
    private UsageQueryTypeSelectorListener listener;

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        queryType = new SimpleComboBox<String>();
        queryType.setEditable(false);
        queryType.setTypeAhead(true);
        queryType.setTriggerAction(TriggerAction.ALL);
        queryType.add(MSGS.accountUsageQueryTypeDays());
        queryType.add(MSGS.accountUsageQueryTypeHour());
        queryType.setSimpleValue(MSGS.accountUsageQueryTypeDays());

        queryType.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
                if (listener != null) {
                    listener.onUpdate();
                }
            }
        });

        add(queryType);
    }

    public UsageQueryTypeSelectorListener getListener() {
        return listener;
    }

    public void setListener(UsageQueryTypeSelectorListener listener) {
        this.listener = listener;
    }

    public QueryType getQueryType() {

        switch (queryType.getSelectedIndex()) {
        case 0: // current billing cycle
            return QueryType.DAY;
        case 1: // last billing cycle
            return QueryType.HOUR;
        default: // default is current
            return QueryType.DAY;
        }
    }
}
