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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import java.util.Date;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

public class DateRangeSelector extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private Date startDate;
    private Date endDate;
    private SplitButton dateRange;
    private DateRangeSelectorListener listener;

    private int currentIndex;

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        dateRange = new SplitButton();
        dateRange.setMinWidth(120);
        dateRange.setStyleAttribute("border", "1px solid thistle");
        dateRange.setStyleAttribute("background-color", "white");
        Menu menu = new Menu();
        menu.add(new MenuItem(MSGS.dataDateRangeLastHour(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        onMenuItemSelected(0, me);
                    }
                }));
        menu.add(new MenuItem(MSGS.dataDateRangeLast12Hours(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        onMenuItemSelected(1, me);
                    }
                }));
        menu.add(new MenuItem(MSGS.dataDateRangeLast24Hours(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        onMenuItemSelected(2, me);
                    }
                }));
        menu.add(new MenuItem(MSGS.dataDateRangeLastWeek(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        onMenuItemSelected(3, me);
                    }
                }));
        menu.add(new MenuItem(MSGS.dataDateRangeLastMonth(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        onMenuItemSelected(4, me);
                    }
                }));

        menu.add(new SeparatorMenuItem());

        menu.add(new MenuItem(MSGS.dataDateRangeCustom(),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent me) {
                        showCustomDateRangeDialog();
                    }
                }));

        dateRange.setMenu(menu);
        dateRange.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                dateRange.showMenu();
            }
        });

        // initialize with the last 24 hours
        onMenuItemSelected(2, null);
        add(dateRange);
    }

    public void nextRange() {
        long delta = getDelta();
        if (endDate == null) {
            endDate = new Date();
        }
        endDate = new Date(endDate.getTime() + delta);
        startDate = new Date(startDate.getTime() + delta);
    }

    public void previousRange() {
        long delta = getDelta();
        if (endDate == null) {
            endDate = new Date();
        }
        endDate = new Date(endDate.getTime() - delta);
        startDate = new Date(startDate.getTime() - delta);
    }

    private long getDelta() {
        switch (currentIndex) {
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

    public DateRangeSelectorListener getListener() {
        return listener;
    }

    public void setListener(DateRangeSelectorListener listener) {
        this.listener = listener;
    }

    private void onMenuItemSelected(int index, MenuEvent me) {
        currentIndex = index;
        Date now = new Date();
        long nowTime = now.getTime();
        switch (index) {
        case 0: // last 1h
            endDate = null;
            startDate = new Date(nowTime - 1L * 60L * 60L * 1000L);
            dateRange.setText(MSGS.dataDateRangeLastHour());
            break;
        case 1: // last 12h
            endDate = null;
            startDate = new Date(nowTime - 12L * 60L * 60L * 1000L);
            dateRange.setText(MSGS.dataDateRangeLast12Hours());
            break;
        case 2: // last 24h
            endDate = null;
            startDate = new Date(nowTime - 24L * 60L * 60L * 1000L);
            dateRange.setText(MSGS.dataDateRangeLast24Hours());
            break;
        case 3: // last week
            endDate = null;
            startDate = new Date(nowTime - 7L * 24L * 60L * 60L * 1000L);
            dateRange.setText(MSGS.dataDateRangeLastWeek());
            break;
        case 4: // last month
            endDate = null;
            startDate = new Date(nowTime - (long) 30L * 24L * 60L * 60L * 1000L);
            dateRange.setText(MSGS.dataDateRangeLastMonth());
            break;
        }
        if (me != null && listener != null) {
            listener.onUpdate();
        }
    }

    public Date getStartDate() {
        long nowTime = new Date().getTime();
        if (startDate == null) {
            return new Date(nowTime - 24L * 60L * 60L * 1000L);
        }
        return startDate;
    }

    public Date getEndDate() {
        if (endDate == null) {
            return new Date();
        }
        return endDate;
    }

    private void showCustomDateRangeDialog() {
        Date end = endDate;
        Date start = startDate;
        if (end == null) {
            end = new Date();
        }
        if (start == null) {
            start = new Date(end.getTime() - 24L * 60L * 60L * 1000L);
        }

        final Dialog dialog = new Dialog();
        dialog.setClosable(false);
        dialog.setBodyBorder(false);
        dialog.setHeading(MSGS.dataDateRangeCustomTitle());
        dialog.setWidth(300);
        dialog.setHeight(200);
        dialog.setHideOnButtonClick(true);
        dialog.setButtons(Dialog.OKCANCEL);
        dialog.setHideOnButtonClick(false);
        dialog.setResizable(false);
        FormData formData = new FormData("-20");
        final FormPanel form = new FormPanel();
        form.setHeaderVisible(false);

        final DateField startDateField = new DateField();
        startDateField.setFieldLabel(MSGS.dataDateRangeStartDate());
        startDateField.setAllowBlank(false);
        startDateField.setValue(start);
        startDateField.setEditable(false);
        startDateField.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        startDateField.setFormatValue(true);
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
        endDateField.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        endDateField.setFormatValue(true);
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
            public String validate(Field<?> field, String value) {
                if (startDateField.getValue().after(endDateField.getValue())) {
                    return MSGS.dataDateRangeInvalidStartDate();
                }
                return null;
            }
        });
        endDateField.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (endDateField.getValue().before(startDateField.getValue())) {
                    return MSGS.dataDateRangeInvalidStopDate();
                }
                return null;
            }
        });
        startTimeField.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (startDateField.getValue().equals(endDateField.getValue()) &&
                        startTimeField.getDateValue().after(endTimeField.getDateValue())) {
                    return MSGS.dataDateRangeInvalidStartTime();
                }
                return null;
            }
        });
        endTimeField.setValidator(new Validator() {

            @Override
            public String validate(Field<?> field, String value) {
                if (startDateField.getValue().equals(endDateField.getValue()) &&
                        endTimeField.getDateValue().before(startTimeField.getDateValue())) {
                    return MSGS.dataDateRangeInvalidStopTime();
                }
                return null;
            }
        });

        dialog.add(form);

        dialog.getButtonById("ok").addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                if (form.isValid()) {
                    long lStartDate = startDateField.getValue().getTime();
                    lStartDate = lStartDate + (long) startTimeField.getValue().getHour() * 60L * 60L * 1000L + (long) startTimeField.getValue().getMinutes() * 60L * 1000L;
                    startDate = new Date(lStartDate);

                    long lEndDate = endDateField.getValue().getTime();
                    lEndDate = lEndDate + (long) endTimeField.getValue().getHour() * 60L * 60L * 1000L + (long) endTimeField.getValue().getMinutes() * 60L * 1000L;
                    endDate = new Date(lEndDate);

                    DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
                    StringBuilder sb = new StringBuilder();
                    sb.append(dtf.format(startDate))
                            .append(" - ")
                            .append(dtf.format(endDate));
                    dateRange.setText(sb.toString());

                    dialog.hide();
                    if (listener != null) {
                        listener.onUpdate();
                    }
                }
            }
        });
        dialog.getButtonById("cancel").addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                dialog.hide();
            }
        });

        dialog.show();
    }
}
