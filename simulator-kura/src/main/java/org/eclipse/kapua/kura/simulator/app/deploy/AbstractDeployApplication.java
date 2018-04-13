/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

import org.eclipse.kapua.kura.simulator.app.Request;
import org.eclipse.kapua.kura.simulator.app.annotated.Application;
import org.eclipse.kapua.kura.simulator.app.annotated.Resource;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kapua.kura.simulator.util.Documents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Application("DEPLOY-V2")
public abstract class AbstractDeployApplication {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDeployApplication.class);

    @Resource
    public void getBundles(final Request request) throws Exception {
        final List<BundleInformation> bundles = getBundles();
        request.replySuccess().send(bundlesToXml(bundles));
    }

    @Resource
    public void executeStart(final Request request) {
        executeOnBundle(request, this::startBundle);
    }

    @Resource
    public void executeStop(final Request request) {
        executeOnBundle(request, this::stopBundle);
    }

    @Resource
    public void getPackages(final Request request) throws Exception {
        final List<DeploymentPackageInformation> packages = getPackages();
        request.replySuccess().send(packagesToXml(packages));
    }

    @Resource
    public void getInstall(final Request request) {
        final InstallState state = getInstallState().orElse(InstallState.IDLE);
        request.replySuccess().send(toMetrics(state));
    }

    @Resource
    public void getDownload(final Request request) {
        final DownloadState state = getDownloadState().orElse(DownloadState.DONE);
        request.replySuccess().send(toMetrics(state));
    }

    public static Map<String, Object> toMetrics(final DownloadState state) {
        final Map<String, Object> metrics = new HashMap<>();

        metrics.put("dp.download.size", state.getTransferSize());
        metrics.put("dp.download.progress", state.getTransferProgress());
        metrics.put("dp.download.status", state.getStatus());
        metrics.put("job.id", state.getJobId());

        return metrics;
    }

    public static Map<String, Object> toMetrics(final InstallState state) {
        final Map<String, Object> metrics = new HashMap<>();

        metrics.put("dp.name", state.getName());
        metrics.put("dp.version", state.getVersion());
        metrics.put("dp.install.status", state.getStatus());
        metrics.put("dp.install.progress", state.getProgress());
        metrics.put("job.id", state.getJobId());

        return metrics;
    }

    @Resource
    public void executeInstall(final Request request) {
        logger.debug("executeInstall");

        final DeploymentInstallPackageRequest installRequest = Metrics.readFrom(new DeploymentInstallPackageRequest(),
                request.getMetrics());

        executeInstall(request, installRequest);

        // we always report success

        request.replySuccess().send();
    }

    @Resource
    public void executeUninstall(final Request request) {
        logger.debug("executeUninstall");

        final DeploymentUninstallPackageRequest uninstallRequest = Metrics
                .readFrom(new DeploymentUninstallPackageRequest(), request.getMetrics());

        executeUninstall(request, uninstallRequest);
    }

    @Resource
    public void executeDownload(final Request request) {
        logger.debug("executeDownload");
        final DeploymentDownloadPackageRequest downloadRequest = Metrics
                .readFrom(new DeploymentDownloadPackageRequest(), request.getMetrics());
        executeDownload(request, downloadRequest);
    }

    @Resource
    public void deleteDownload(final Request request) {
        cancelDownload(request);
    }

    protected abstract boolean startBundle(long bundleId);

    protected abstract boolean stopBundle(long bundleId);

    protected abstract List<BundleInformation> getBundles();

    protected abstract List<DeploymentPackageInformation> getPackages();

    protected abstract void executeDownload(Request request, DeploymentDownloadPackageRequest downloadRequest);

    protected abstract void executeInstall(Request request, DeploymentInstallPackageRequest installRequest);

    protected abstract void executeUninstall(Request request, DeploymentUninstallPackageRequest uninstallRequest);

    protected abstract Optional<DownloadState> getDownloadState();

    protected abstract Optional<InstallState> getInstallState();

    protected abstract void cancelDownload(Request request);

    public void executeOnBundle(final Request request, final Function<Long, Boolean> consumer) {
        final long bundleId = Long.parseLong(request.renderTopic(2));
        if (consumer.apply(bundleId)) {
            request.replySuccess().send();
        } else {
            request.replyNotFound();
        }
    }

    protected String packagesToXml(final List<DeploymentPackageInformation> packages) throws Exception {
        return Documents.create(doc -> fillPackagesDocument(doc, packages));
    }

    protected void fillPackagesDocument(final Document doc, final List<DeploymentPackageInformation> packages) {
        final Element ps = doc.createElement("packages");
        doc.appendChild(ps);

        for (final DeploymentPackageInformation dp : packages) {
            final Element p = doc.createElement("package");
            ps.appendChild(p);

            addValue(p, "name", dp.getSymbolicName());
            addValue(p, "version", dp.getVersion());

            final Element bs = doc.createElement("bundles");
            p.appendChild(bs);

            for (final DeploymentPackageInformation.BundleInformation bi : dp.getBundles()) {
                final Element b = doc.createElement("bundle");
                bs.appendChild(b);

                addValue(b, "name", bi.getSymbolicName());
                addValue(b, "version", bi.getVersion());
            }
        }
    }

    protected String bundlesToXml(final List<BundleInformation> bundles) throws Exception {
        return Documents.create(doc -> fillBundlesDocument(doc, bundles));
    }

    protected void fillBundlesDocument(final Document doc, final List<BundleInformation> bundles) {
        final Element bs = doc.createElement("bundles");
        doc.appendChild(bs);

        for (final BundleInformation bi : bundles) {
            final Element b = doc.createElement("bundle");
            bs.appendChild(b);
            addValue(b, "name", bi.getSymbolicName());
            addValue(b, "version", bi.getVersion());
            addValue(b, "id", Long.toString(bi.getId()));
            addValue(b, "state", bi.getStateString());
        }
    }

    protected static void addValue(final Element b, final String name, final String value) {
        final Element ele = b.getOwnerDocument().createElement(name);
        b.appendChild(ele);
        ele.setTextContent(value);
    }

}
