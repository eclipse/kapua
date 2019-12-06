/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;

/**
 * {@link DeviceLifecyclePayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraNotifyPayload extends KuraAppPayload implements DeviceNotifyPayload {

    @Override
    public Long getOperationId() {
        if (getMetrics().containsKey(KuraNotifyMetrics.OPERATION_ID.getValue())) {
            return (Long) getMetrics().get(KuraNotifyMetrics.OPERATION_ID.getValue());
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.OPERATION_ID_ALTERNATIVE.getValue())) {
            return (Long) getMetrics().get(KuraNotifyMetrics.OPERATION_ID_ALTERNATIVE.getValue());
        }

        return null;
    }

    public String getResource() {

        if (getMetrics().containsKey(KuraNotifyMetrics.DOWNLOAD_STATUS.getValue())) {
            return "download";
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.INSTALL_STATUS.getValue())) {
            return "install";
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.UNINSTALL_PROGRESS.getValue())) {
            return "uninstall";
        }

        return null;
    }

    @Override
    public String getStatus() {
        Object status = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_STATUS.getValue());

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.INSTALL_STATUS.getValue());
        }

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.UNINSTALL_STATUS.getValue());
        }

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.STATUS.getValue());
        }

        return (String) status;
    }

    @Override
    public Integer getProgress() {
        Object progress = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_PROGRESS.getValue());

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.INSTALL_PROGRESS.getValue());
        }

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.UNINSTALL_PROGRESS.getValue());
        }

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.PROGRESS.getValue());
        }

        return (Integer) progress;
    }

    @Override
    public String getMessage() {
        Object message = getMetrics().get(KuraNotifyMetrics.NOTIFY_MESSAGE.getValue());

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_ERROR_MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.INSTALL_MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.INSTALL_ERROR_MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.UNINSTALL_MESSAGE.getValue());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.UNINSTALL_ERROR_MESSAGE.getValue());
        }

        return (String) message;
    }

    @Override
    public String toDisplayString() {
        return null;
    }
}