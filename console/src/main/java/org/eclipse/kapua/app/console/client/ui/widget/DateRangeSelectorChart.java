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
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;

public class DateRangeSelectorChart extends LayoutContainer
{

    private static final ConsoleMessages MSGS           = GWT.create(ConsoleMessages.class);

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
        menu.add(new MenuItem(MSGS.dataEndNow(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(0, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataEndHour(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(1, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataEnd12Hour(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(2, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataEndDay(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(3, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataEndWeek(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(4, me);
                                  }
                              }));
        menu.add(new MenuItem(MSGS.dataEndMonth(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      onMenuItemSelected(5, me);
                                  }
                              }));

        menu.add(new SeparatorMenuItem());

        menu.add(new MenuItem(MSGS.dataEndCustom(),
                              new SelectionListener<MenuEvent>() {
                                  @Override
                                  public void componentSelected(MenuEvent me)
                                  {
                                      showCustomDateRangeDialog();
                                  }
                              }));

        m_dateRange.setMenu(menu);

        // initialize with the last 24 hours
        onMenuItemSelected(0, null);
        add(m_dateRange);
    }

    public void nextRange()
    {
        long delta = getDelta();
        if (m_endDate == null) {
            m_endDate = new Date();
        }
        m_endDate = new Date(m_endDate.getTime() + delta);
    }

    public void previousRange()
    {
        long delta = getDelta();
        if (m_endDate == null) {
            m_endDate = new Date();
        }
        m_endDate = new Date(m_endDate.getTime() - delta);
    }

    private long getDelta()
    {
        switch (m_currentIndex) {
            case 1: // last 1h
                return 1L * 60L * 60L * 1000L;
            case 2: // last 12h
                return 12L * 60L * 60L * 1000L;
            case 4: // last 24h
                return 24L * 60L * 60L * 1000L;
            case 5: // last week
                return 7L * 24L * 60L * 60L * 1000L;
            case 6: // last month
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
            case 0: // now
                m_endDate = new Date(nowTime);
                m_dateRange.setText(MSGS.dataEndNow());
                break;
            case 1: // last 1h
                m_endDate = new Date(nowTime - 1L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataEndHour());
                break;
            case 2: // last 12h
                m_endDate = new Date(nowTime - 12L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataEnd12Hour());
                break;
            case 3: // last 24h
                m_endDate = new Date(nowTime - 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataEndDay());
                break;
            case 4: // last week
                m_endDate = new Date(nowTime - 7L * 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataEndWeek());
                break;
            case 5: // last month
                m_endDate = new Date(nowTime - (long) 30L * 24L * 60L * 60L * 1000L);
                m_dateRange.setText(MSGS.dataEndMonth());
                break;
        }
        if (me != null && m_listener != null) {
            m_listener.onUpdate();
        }
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
        if (end == null) {
            end = new Date();
        }

        final Dialog dialog = new Dialog();
        dialog.setBodyBorder(false);
        dialog.setHeading(MSGS.dataDateRangeCustomTitle());
        dialog.setWidth(300);
        dialog.setHeight(145);
        dialog.setHideOnButtonClick(true);
        dialog.setButtons(Dialog.OKCANCEL);
        dialog.setHideOnButtonClick(false);
        dialog.setModal(true);

        FormData formData = new FormData("-20");
        final FormPanel form = new FormPanel();
        form.setHeaderVisible(false);

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

        dialog.add(form);

        dialog.getButtonById("ok").addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce)
            {
                if (form.isValid()) {
                    long lEndDate = endDateField.getValue().getTime();
                    lEndDate = lEndDate + (long) endTimeField.getValue().getHour() * 60L * 60L * 1000L + (long) endTimeField.getValue().getMinutes() * 60L * 1000L;
                    m_endDate = new Date(lEndDate);

                    DateTimeFormat dtf = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
                    StringBuilder sb = new StringBuilder();
                    sb.append(dtf.format(m_endDate));
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
