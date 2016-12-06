/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.ui.widget;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class UsageQueryTypeSelector extends LayoutContainer
{

    public enum QueryType
    {
        DAY,
        HOUR
    }

    private static final ConsoleMessages   MSGS = GWT.create(ConsoleMessages.class);

    private SimpleComboBox<String>         m_queryType;
    private UsageQueryTypeSelectorListener m_listener;

    protected void onRender(Element parent, int index)
    {

        super.onRender(parent, index);

        m_queryType = new SimpleComboBox<String>();
        m_queryType.setEditable(false);
        m_queryType.setTypeAhead(true);
        m_queryType.setTriggerAction(TriggerAction.ALL);
        m_queryType.add(MSGS.accountUsageQueryTypeDays());
        m_queryType.add(MSGS.accountUsageQueryTypeHour());
        m_queryType.setSimpleValue(MSGS.accountUsageQueryTypeDays());

        m_queryType.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se)
            {
                if (m_listener != null) {
                    m_listener.onUpdate();
                }
            }
        });

        add(m_queryType);
    }

    public UsageQueryTypeSelectorListener getListener()
    {
        return m_listener;
    }

    public void setListener(UsageQueryTypeSelectorListener listener)
    {
        m_listener = listener;
    }

    public QueryType getQueryType()
    {

        switch (m_queryType.getSelectedIndex()) {
            case 0: // current billing cycle
                return QueryType.DAY;
            case 1: // last billing cycle
                return QueryType.HOUR;
            default: // default is current
                return QueryType.DAY;
        }
    }
}
