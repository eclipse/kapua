/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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
package org.eclipse.kapua.app.console.shared.service;

import java.util.List;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceCommandInput;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceCommandOutput;
import org.eclipse.kapua.app.console.shared.model.GwtSnapshot;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.device.management.bundles.GwtBundle;
import org.eclipse.kapua.app.console.shared.model.device.management.packages.GwtPackageInstallRequest;
import org.eclipse.kapua.app.console.shared.model.device.management.packages.GwtPackageOperation;
import org.eclipse.kapua.app.console.shared.model.device.management.packages.GwtPackageUninstallRequest;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deviceManagement")
public interface GwtDeviceManagementService extends RemoteService {

    //
    // Packages
    //
    /**
     * Returns the packages installed on a Device.
     * 
     * @param scopeShortId
     *            device scope id in short form
     * @param deviceShortId
     *            device id in short form
     * 
     * @return list of installed packages installed on the selected device
     * 
     * @throws GwtKapuaException
     * 
     * @since 1.0.0
     */
    public List<GwtDeploymentPackage> findDevicePackages(String scopeShortId, String deviceShortId)
            throws GwtKapuaException;

    /**
     * Install a package into a device.
     * 
     * @param xsrfToken
     *            XARF token associated with the request
     * @param gwtPackageInstallRequest
     *            the package install request
     * 
     * @throws GwtKapuaException
     * 
     * @since 1.0.0
     */
    public void installPackage(GwtXSRFToken xsrfToken, GwtPackageInstallRequest gwtPackageInstallRequest)
            throws GwtKapuaException;

    /**
     * Get the status of downloads operations running on the device
     * 
     * @param scopeShortId
     *            device scope id in short form
     * @param deviceShortId
     *            device id in short form
     * @return list of current running download operation on the device
     * 
     * @throws GwtKapuaException
     * 
     * @since 1.0.0
     */
    public ListLoadResult<GwtPackageOperation> getDownloadOperations(String scopeShortId, String deviceShortId)
            throws GwtKapuaException;

    /**
     * Uninstalls a package from a device.
     * 
     * @param xsrfToken
     *            XARF token associated with the request
     * @param gwtPackageUninstallRequest
     *            the package uninstall request
     * 
     * @throws GwtKapuaException
     * 
     * @since 1.0.0
     */
    public void uninstallPackage(GwtXSRFToken xsrfToken, GwtPackageUninstallRequest gwtPackageUninstallRequest)
            throws GwtKapuaException;

    //
    // Configurations
    //
    /**
     * Returns the configuration of a Device as the list of all the configurable components.
     * 
     * @param device
     * @return
     */
    public List<GwtConfigComponent> findDeviceConfigurations(GwtDevice device)
            throws GwtKapuaException;

    /**
     * Updates the configuration of the provided component.
     * 
     * @param device
     * @param configComponent
     */
    public void updateComponentConfiguration(GwtXSRFToken xsrfToken, GwtDevice device, GwtConfigComponent configComponent)
            throws GwtKapuaException;

    //
    // Snapshots
    //
    /**
     * 
     * @param device
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtSnapshot> findDeviceSnapshots(GwtDevice device)
            throws GwtKapuaException;

    /**
     * 
     * @param device
     * @param snapshot
     * @throws GwtKapuaException
     */
    public void rollbackDeviceSnapshot(GwtXSRFToken xsfrToken, GwtDevice device, GwtSnapshot snapshot)
            throws GwtKapuaException;

    //
    // Bundles
    //
    public ListLoadResult<GwtBundle> findBundles(GwtDevice device)
            throws GwtKapuaException;

    public void startBundle(GwtXSRFToken xsfrToken, GwtDevice device, GwtBundle pair)
            throws GwtKapuaException;

    public void stopBundle(GwtXSRFToken xsfrToken, GwtDevice device, GwtBundle pair)
            throws GwtKapuaException;

    //
    // Commands
    //
    /**
     * Executes a command on a remote Device.
     * 
     * @param xsfrToken
     * @param device
     * @param commandInput
     * @return
     */
    public GwtDeviceCommandOutput executeCommand(GwtXSRFToken xsfrToken, GwtDevice device, GwtDeviceCommandInput commandInput)
            throws GwtKapuaException;

}
