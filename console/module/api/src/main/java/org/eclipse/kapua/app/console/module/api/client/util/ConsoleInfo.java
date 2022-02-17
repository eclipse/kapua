/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.client.util;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class ConsoleInfo extends Info {
    private static final int CHAR_STEP = 20;
    private static final int STEP_HEIGHT = 18;

    //Private static instance of ConsoleInfo singleton class
    private static final ConsoleInfo SINGLE_INSTANCE = new ConsoleInfo();
    private static Timer timer;

    /**
     * Private ConsoleInfo class constructor
     */
    private ConsoleInfo() {

    }

    /**
     * Method for returning ConsoleInfo singleton instance.
     * @return static instance of ConsoleInfo class
     */
    public static ConsoleInfo getInstance() {
        return SINGLE_INSTANCE;
    }

    public static void display(String title, String text) {
        display(title, text, 5000);
    }

    public static void display(String title, String text, int delay) {
        display(title, text, delay, true);
    }

    public static void display(String title, String text, int delay,
                               boolean keepOnMouseOver) {
        //
        // XSS escaping
        if (text != null) {
            text = KapuaSafeHtmlUtils.htmlEscape(text);
        }

        int infoHeight = 25 + STEP_HEIGHT * (1 + ((text != null ? text.length() : 0) / CHAR_STEP)) + STEP_HEIGHT / 3;

        InfoConfig config = new InfoConfig(title, text);
        config.height = infoHeight;

        if (delay > 0) {
            config.display = delay;
        }

        Params params = new Params();
        params.set("keepOnMouseOver", keepOnMouseOver);
        config.params = params;

        displayInfo(config);
    }

    /**
     * Displays a message using the specified config.
     *
     * @param config instance of InfoConfig class
     */
    public static void displayInfo(InfoConfig config) {
        ConsoleInfo.getInstance().show(config);
    }

    public static void hideInfo() {
        ConsoleInfo.getInstance().afterHide();
    }

    @Override
    protected void onShowInfo() {
        RootPanel.get().add(this);
        el().makePositionable(true);
        setTitle();
        setText();

        Point p = position();
        el().setLeftTop(p.x, p.y);
        setSize(config.width, config.height);

        afterShow();
    }

    @Override
    protected void afterShow() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer() {
            public void run() {
                afterHide();
            }
        };
        timer.schedule(config.display);
    }

    private void setTitle() {
        if (config.title != null) {
            head.setVisible(true);
            if (config.params != null) {
                config.title = Format.substitute(config.title, config.params);
            }
            setHeading(config.title);
            } else {
                head.setVisible(false);
            }
        }

    private void setText() {
        if (config.text != null) {
            if (config.params != null) {
                config.text = Format.substitute(config.text, config.params);
            }
            removeAll();
            addText(config.text);
            }
    }

    protected void afterHide() {
        RootPanel.get().remove(this);
    }
}
