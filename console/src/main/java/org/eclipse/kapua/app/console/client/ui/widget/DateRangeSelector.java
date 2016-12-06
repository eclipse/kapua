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

import java.util.Date;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;

public class DateRangeSelector extends LayoutContainer
{

    private static final ConsoleMessages MSGS           = GWT.create(ConsoleMessages.class);

    private Date                         m_startDate;
    private Date                         m_endDate;
    private SplitButton                  m_dateRange;
    private DateRangeSelectorListener    m_listener;

    private int                          m_currentIndex = 0;

    protected void onRender(Element parent, int index)
    {

        super.onRender(parent, index);

        m_dateRange = new SplitButton();
        m_dateRange.setMinWidth(120);
        m_dateRange.setStyleAttribute("border", "1px solid thistle");
        m_dateRange.setStyleAttribute("background-color", "white");
        Menu menu = new Menu();
        menu.add(new MenuItem(MSGS.dataDateRangeLastHour(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(0, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataDateRangeLast12Hours(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(1, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataDateRangeLast24Hours(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(2, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataDateRangeLastWeek(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(3, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataDateRangeLastMonth(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(4, me);
                                  }
                              }));

        menu.add(new SeparatorMenuItem());

        menu.add(new MenuItem(MSGS.dataDateRangeCustom(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      showCustomDateRangeDialog();
                                  }
                              }));

        m_dateRange.setMenu(menu);

        // initialize with the last 24 hours
        onMenuItemSelected(2, null);
        add(m_dateRange);
    }

    public void nextRange()
    {
        long delta = getDelta();
        if (m_endDate == null) {
            m_endDate = new Date();
        }
        m_endDate = new Date(m_endDate.getTime() + delta);
        m_startDate = new Date(m_startDate.getTime() + delta);
    }

    public void previousRange()
    {
        long delta = getDelta();
        if (m_endDate == null) {
            m_endDate = new Date();
        }
        m_endDate = new Date(m_endDate.getTime() - delta);
        m_startDate = new Date(m_startDate.getTime() - delta);
    }

    private long getDelta()
    {
        switch (m_currentIndex) {
            case 0: // last 1h
                return 1L * 60L * 60L * 1000L;
            case 1: // last 12h
                return 12L * 60L * 60L * 1000L;
            case 2: // last 24h
                return 24L * 60L * 60L * 1000L;
            case 3: // last week
                return 7L * 24L * 60L * 60L * 1000L;
            case 4: // last month
                return 30L * 24L * 60L * 60L * 1000L;
        }
        return 0;
    }

    public DateRangeSelectorListener getListener()
    {
        return m_listener;
    }

    public void setListener(DateRangeSelectorListener listener)
    {
        m_listener = listener;
    }

    private void onMenuItemSelected(int index, MenuEvent me)
    {
        m_currentIndex = index;
        Date now = new Date();
        long nowTime = now.getTime();
        switch (index) {
            case 0: // last 1h
                m_endDate = null;
                m_startDate = new Date(nowTime - 1L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataDateRangeLastHour());
                break;
            case 1: // last 12h
                m_endDate = null;
                m_startDate = new Date(nowTime - 12L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataDateRangeLast12Hours());
                break;
            case 2: // last 24h
                m_endDate = null;
                m_startDate = new Date(nowTime - 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataDateRangeLast24Hours());
                break;
            case 3: // last week
                m_endDate = null;
                m_startDate = new Date(nowTime - 7L * 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataDateRangeLastWeek());
                break;
            case 4: // last month
                m_endDate = null;
                m_startDate = new Date(nowTime - (long) 30L * 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataDateRangeLastMonth());
                break;
        }
        if (me != null && m_listener != null) {
            m_listener.onUpdate();
        }
    }

    public Date getStartDate()
    {
        long nowTime = new Date().getTime();
        if (m_startDate == null) {
            return new Date(nowTime - 24L * 60L * 60L * 1000L);
        }
        return m_startDate;
    }

    public Date getEndDate()
    {
        if (m_endDate == null) {
            return new Date();
        }
        return m_endDate;
    }

    private void showCustomDateRangeDialog()
    {
        Date end = m_endDate;
        Date start = m_startDate;
        if (end == null) {
            end = new Date();
        }
        if (start == null) {
            start = new Date(end.getTime() - 24L * 60L * 60L * 1000L);
        }

        final Dialog dialog = new Dialog();
        dialog.setBodyBorder(false);
        dialog.setHeading(MSGS.dataDateRangeCustomTitle());
        dialog.setWidth(300);
        dialog.setHeight(200);
        dialog.setHideOnButtonClick(true);
        dialog.setButtons(Dialog.OKCANCEL);
        dialog.setHideOnButtonClick(false);

        FormData formData = new FormData("-20");
        final FormPanel form = new FormPanel();
        form.setHeaderVisible(false);

        final DateField startDateField = new DateField();
        startDateField.setFieldLabel(MSGS.dataDateRangeStartDate());
        startDateField.setAllowBlank(false);
        startDateField.setValue(start);
        startDateField.setEditable(false);
        form.add(startDateField, formData);

        final TimeField startTimeField = new TimeField();
        startTimeField.setFormat(DateTimeFormat.getFormat("HH:mm"));
        startTimeField.setTriggerAction(TriggerAction.ALL);
        startTimeField.setFieldLabel(MSGS.dataDateRangeStartTime());
        startTimeField.setAllowBlank(false);
        startTimeField.setDateValue(start);
        startTimeField.setEditable(false);
        form.add(startTimeField, formData);

        final DateField endDateField = new DateField();
        endDateField.setFieldLabel(MSGS.dataDateRangeStopDate());
        endDateField.setAllowBlank(false);
        endDateField.setValue(end);
        endDateField.setEditable(false);
        form.add(endDateField, formData);

        final TimeField endTimeField = new TimeField();
        endTimeField.setFormat(DateTimeFormat.getFormat("HH:mm"));
        endTimeField.setTriggerAction(TriggerAction.ALL);
        endTimeField.setFieldLabel(MSGS.dataDateRangeStopTime());
        endTimeField.setAllowBlank(false);
        endTimeField.setDateValue(end);
        endTimeField.setEditable(false);
        form.add(endTimeField, formData);

        startDateField.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value)
            {
                if (startDateField.getValue().after(endDateField.getValue())) {
                    return MSGS.dataDateRangeInvalidStartDate();
                }
                return null;
            }
        });
        endDateField.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value)
            {
                if (endDateField.getValue().before(startDateField.getValue())) {
                    return MSGS.dataDateRangeInvalidStopDate();
                }
                return null;
            }
        });
        startTimeField.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value)
            {
                if (startDateField.getValue().equals(endDateField.getValue()) &&
                    startTimeField.getDateValue().after(endTimeField.getDateValue())) {
                    return MSGS.dataDateRangeInvalidStartTime();
                }
                return null;
            }
        });
        endTimeField.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value)
            {
                if (startDateField.getValue().equals(endDateField.getValue()) &&
                    endTimeField.getDateValue().before(startTimeField.getDateValue())) {
                    return MSGS.dataDateRangeInvalidStopTime();
                }
                return null;
            }
        });

        dialog.add(form);

        dialog.getButtonById("ok").addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce)
            {
                if (form.isValid()) {
                    long lStartDate = startDateField.getValue().getTime();
                    lStartDate = lStartDate + (long) startTimeField.getValue().getHour() * 60L * 60L * 1000L + (long) startTimeField.getValue().getMinutes() * 60L * 1000L;
                    m_startDate = new Date(lStartDate);

                    long lEndDate = endDateField.getValue().getTime();
                    lEndDate = lEndDate + (long) endTimeField.getValue().getHour() * 60L * 60L * 1000L + (long) endTimeField.getValue().getMinutes() * 60L * 1000L;
                    m_endDate = new Date(lEndDate);

                    DateTimeFormat dtf = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
                    StringBuilder sb = new StringBuilder();
                    sb.append(dtf.format(m_startDate))
                      .append(" - ")
                      .append(dtf.format(m_endDate));
                    m_dateRange.setText(sb.toString());

                    dialog.hide();
                    if (m_listener != null) {
                        m_listener.onUpdate();
                    }
                }
            }
        });
        dialog.getButtonById("cancel").addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce)
            {
                dialog.hide();
            }
        });

        dialog.show();
    }
}
