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
package org.eclipse.kapua.service.device.call.message.kura.management.deploy;

import org.eclipse.kapua.service.device.call.message.kura.management.KuraNotifyPayload;

public class KuraNotifyPackageInstallPayload extends KuraNotifyPayload {

    @Override
    protected String getStatusMetricName() {
        return "dp.install.status";
    }

    @Override
    protected String getProgressMetricName() {
        return "dp.install.progress";
    }

    @Override
    protected String getErrorMetricName() {
        return "dp.install.error.message";
    }
}
