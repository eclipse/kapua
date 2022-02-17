/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.kapua.kura.simulator.app.deploy.DownloadSimulator;
import org.eclipse.scada.utils.concurrent.NamedThreadFactory;

public class TestDownloadSimulator {

    private TestDownloadSimulator() {
    }

    public static void main(final String[] args) throws Exception {
        final ScheduledExecutorService executor = Executors
                .newSingleThreadScheduledExecutor(new NamedThreadFactory("DownloadSimulator"));

        try (final DownloadSimulator sim = new DownloadSimulator(executor, 10 * 1024)) {
            sim.startDownload(1, 1024 * 1024, System.out::println, () -> {
                System.out.println("Download completed!");
            });
            Thread.sleep(10_000);
            sim.cancelDownload();
            System.out.println(sim.getState());
            Thread.sleep(120_000);
        }

        executor.shutdown();
    }
}
