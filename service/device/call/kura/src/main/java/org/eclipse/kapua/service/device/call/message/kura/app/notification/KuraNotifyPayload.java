/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppPayload;

/**
 * {@link DeviceNotifyPayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraNotifyPayload extends KuraAppPayload implements DeviceNotifyPayload {

    @Override
    public Long getOperationId() {
        if (getMetrics().containsKey(KuraNotifyMetrics.OPERATION_ID.getName())) {
            return (Long) getMetrics().get(KuraNotifyMetrics.OPERATION_ID.getName());
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.OPERATION_ID_ALTERNATIVE.getName())) {
            return (Long) getMetrics().get(KuraNotifyMetrics.OPERATION_ID_ALTERNATIVE.getName());
        }

        return null;
    }

    public String getResource() {

        if (getMetrics().containsKey(KuraNotifyMetrics.DOWNLOAD_STATUS.getName())) {
            return "download";
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.INSTALL_STATUS.getName())) {
            return "install";
        }

        if (getMetrics().containsKey(KuraNotifyMetrics.UNINSTALL_PROGRESS.getName())) {
            return "uninstall";
        }

        return null;
    }

    @Override
    public String getStatus() {
        Object status = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_STATUS.getName());

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.INSTALL_STATUS.getName());
        }

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.UNINSTALL_STATUS.getName());
        }

        if (status == null) {
            status = getMetrics().get(KuraNotifyMetrics.STATUS.getName());
        }

        return (String) status;
    }

    @Override
    public Integer getProgress() {
        Object progress = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_PROGRESS.getName());

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.INSTALL_PROGRESS.getName());
        }

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.UNINSTALL_PROGRESS.getName());
        }

        if (progress == null) {
            progress = getMetrics().get(KuraNotifyMetrics.PROGRESS.getName());
        }

        return (Integer) progress;
    }

    @Override
    public String getMessage() {
        Object message = getMetrics().get(KuraNotifyMetrics.NOTIFY_MESSAGE.getName());

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.DOWNLOAD_ERROR_MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.INSTALL_MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.INSTALL_ERROR_MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.UNINSTALL_MESSAGE.getName());
        }

        if (message == null) {
            message = getMetrics().get(KuraNotifyMetrics.UNINSTALL_ERROR_MESSAGE.getName());
        }

        return (String) message;
    }
}
