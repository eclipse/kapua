/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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
        return (Long) getMetrics().get(KuraNotifyMetrics.OPERATION_ID.getValue());
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

        return (Integer) progress;
    }

    @Override
    public String toDisplayString() {
        return null;
    }
}