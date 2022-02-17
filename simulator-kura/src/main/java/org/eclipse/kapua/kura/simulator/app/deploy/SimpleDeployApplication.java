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
package org.eclipse.kapua.kura.simulator.app.deploy;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.eclipse.kapua.kura.simulator.app.Request;
import org.osgi.framework.Bundle;

public class SimpleDeployApplication extends AbstractDeployApplication implements AutoCloseable {

    public static class BundleState {

        private final String symbolicName;
        private final String version;
        private int state;

        public BundleState(final String symbolicName, final String version, final int state) {
            this.symbolicName = symbolicName;
            this.version = version;
            this.state = state;
        }

        public int getState() {
            return this.state;
        }

        public void setState(final int state) {
            this.state = state;
        }

        public String getSymbolicName() {
            return this.symbolicName;
        }

        public String getVersion() {
            return this.version;
        }
    }

    private final Map<Long, BundleState> bundles = new TreeMap<>();
    private final List<DeploymentPackageInformation> packages = new LinkedList<>();
    private final DownloadSimulator downloadSimulator;
    private long nextFreeBundleId;

    public SimpleDeployApplication(final ScheduledExecutorService downloadExecutor) {
        // Sonar java:S2184
        this.downloadSimulator = new DownloadSimulator(downloadExecutor, (long)10 * 1024);

        this.bundles.put(this.nextFreeBundleId++, new BundleState("org.osgi", "6.0.0", Bundle.ACTIVE));
        this.bundles.put(this.nextFreeBundleId++, new BundleState("org.eclipse.kura.api", "2.1.0", Bundle.ACTIVE));
        this.bundles.put(this.nextFreeBundleId++, new BundleState("org.eclipse.kura.core", "2.1.1", Bundle.ACTIVE));
        this.bundles.put(this.nextFreeBundleId++,
                new BundleState("org.eclipse.kura.unresolved", "2.1.2", Bundle.INSTALLED));
        this.bundles.put(this.nextFreeBundleId++,
                new BundleState("org.eclipse.kura.unstarted", "2.1.1", Bundle.RESOLVED));
    }

    public SimpleDeployApplication(final ScheduledExecutorService downloadExecutor, final List<BundleState> bundles) {
        // Sonar java:S2184
        this.downloadSimulator = new DownloadSimulator(downloadExecutor, (long)10 * 1024);

        if (bundles != null) {
            for (final BundleState bundle : bundles) {
                internalInstallBundle(bundle);
            }
        }
    }

    @Override
    public void close() {
        this.downloadSimulator.close();
    }

    @Override
    protected void executeDownload(final Request request, final DeploymentDownloadPackageRequest downloadRequest) {
        request.replySuccess().send();

        // Sonar java:S2184
        final boolean started = this.downloadSimulator.startDownload(downloadRequest.getJobId(), (long)128 * 1024, state ->
            request.notification("download").send(toMetrics(state)), () ->
            internalInstallPackage(downloadRequest.getName(), downloadRequest.getVersion(),
                    bundles(downloadRequest.getName(), downloadRequest.getVersion(), 10))
        );

        if (!started) {
            request.replyError().send(Collections.singletonMap("download.status", "IN_PROGRESS"),
                    "Download already in progress".getBytes(StandardCharsets.UTF_8));
        } else {
            request.replySuccess().send();
        }
    }

    private List<org.eclipse.kapua.kura.simulator.app.deploy.DeploymentPackageInformation.BundleInformation> bundles(
            final String baseName, final String version, final int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        final List<org.eclipse.kapua.kura.simulator.app.deploy.DeploymentPackageInformation.BundleInformation> result = new ArrayList<>(
                count);

        for (int i = 0; i < count; i++) {
            final String name = String.format("%s.%s", baseName, i);
            result.add(new org.eclipse.kapua.kura.simulator.app.deploy.DeploymentPackageInformation.BundleInformation(
                    name, version));
        }

        return result;
    }

    @Override
    protected void cancelDownload(final Request request) {
        if (this.downloadSimulator.cancelDownload()) {
            request.replySuccess().send();
        }
    }

    @Override
    protected void executeInstall(final Request request, final DeploymentInstallPackageRequest installRequest) {
        // FIXME: implement
        request.replySuccess().send();
    }

    @Override
    protected void executeUninstall(final Request request, final DeploymentUninstallPackageRequest uninstallRequest) {
        request.replySuccess().send();

        internalUninstallPackage(uninstallRequest.getName(), uninstallRequest.getVersion());

        final Map<String, Object> metrics = new HashMap<>();

        metrics.put("dp.uninstall.status", "COMPLETED");
        metrics.put("dp.uninstall.progress", 100);
        metrics.put("dp.name", uninstallRequest.getName());
        metrics.put("job.id", uninstallRequest.getJobId());

        request.notification("uninstall").send(metrics);
    }

    protected void internalInstallPackage(final String name, final String version,
            final List<DeploymentPackageInformation.BundleInformation> bundles) {
        internalUninstallPackage(name, version);

        this.packages.add(new DeploymentPackageInformation(name, version, Instant.now(), bundles));

        for (final DeploymentPackageInformation.BundleInformation bi : bundles) {
            internalInstallBundle(new BundleState(bi.getSymbolicName(), bi.getVersion(), Bundle.ACTIVE));
        }
    }

    protected void internalUninstallPackage(final String name, final String version) {
        final Iterator<DeploymentPackageInformation> i = this.packages.iterator();
        while (i.hasNext()) {
            final DeploymentPackageInformation p = i.next();

            if (p.getSymbolicName().equals(name) && p.getVersion().equals(version)) {
                i.remove();
                for (final DeploymentPackageInformation.BundleInformation bi : p.getBundles()) {
                    internallUninstallBundle(bi.getSymbolicName(), bi.getVersion());
                }

            }
        }
    }

    protected void internalInstallBundle(final BundleState bundle) {
        this.bundles.put(this.nextFreeBundleId++, bundle);
    }

    protected void internallUninstallBundle(final String symbolicName, final String version) {
        final Iterator<Entry<Long, BundleState>> i = this.bundles.entrySet().iterator();
        while (i.hasNext()) {
            final Entry<Long, BundleState> e = i.next();
            final BundleState b = e.getValue();
            if (b.getSymbolicName().equals(symbolicName) && b.getVersion().equals(version)) {
                i.remove();
                // only remove the first one
                return;
            }
        }
    }

    @Override
    protected List<DeploymentPackageInformation> getPackages() {
        return Collections.unmodifiableList(this.packages);
    }

    @Override
    protected Optional<DownloadState> getDownloadState() {
        return Optional.ofNullable(this.downloadSimulator.getState());
    }

    @Override
    protected Optional<InstallState> getInstallState() {
        return Optional.empty(); // FIXME: simulate installation state
    }

    @Override
    protected List<BundleInformation> getBundles() {
        return this.bundles.entrySet()
                .stream().map(entry -> new BundleInformation(entry.getValue().getSymbolicName(),
                        entry.getValue().getVersion(), entry.getKey(), entry.getValue().getState()))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean startBundle(final long bundleId) {
        final BundleState bundle = this.bundles.get(bundleId);
        if (bundle == null) {
            return false;
        }

        if (bundle.getState() == Bundle.RESOLVED) {
            bundle.setState(Bundle.ACTIVE);
        }

        return true;
    }

    @Override
    protected boolean stopBundle(final long bundleId) {
        final BundleState bundle = this.bundles.get(bundleId);
        if (bundle == null) {
            return false;
        }

        if (bundle.getState() == Bundle.ACTIVE) {
            bundle.setState(Bundle.RESOLVED);
        }

        return true;
    }

}
