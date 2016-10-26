/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.shared.util;

import java.net.URISyntaxException;
import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionPredicates;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.user.User;

public class KapuaGwtConverter {

    public static GwtAccount convert(Account account)
            throws KapuaException {
        GwtAccount gwtAccount = new GwtAccount();

        gwtAccount.setId(account.getId().getShortId());
        gwtAccount.setName(account.getName());
        gwtAccount.setCreatedOn(account.getCreatedOn());
        gwtAccount.setCreatedBy(account.getCreatedBy().getShortId());
        gwtAccount.setModifiedOn(account.getModifiedOn());
        gwtAccount.setModifiedBy(account.getModifiedBy().getShortId());
        gwtAccount.setGwtOrganization(convert(account.getOrganization()));
        gwtAccount.setParentAccountId(account.getScopeId() != null ? account.getScopeId().getShortId() : null);
        gwtAccount.setOptlock(account.getOptlock());

        try {
            gwtAccount.setBrokerURL(SystemUtils.getBrokerURI().toString());
        } catch (URISyntaxException use) {
            gwtAccount.setBrokerURL("");
        }

        Properties accountProperties = account.getEntityProperties();
        gwtAccount.setDashboardPreferredTopic(accountProperties.getProperty("topic"));
        gwtAccount.setDashboardPreferredMetric(accountProperties.getProperty("metric"));

        return gwtAccount;
    }

    public static GwtOrganization convert(Organization organization) {
        GwtOrganization gwtOrganization = new GwtOrganization();

        gwtOrganization.setName(organization.getName());
        gwtOrganization.setPersonName(organization.getPersonName());
        gwtOrganization.setEmailAddress(organization.getEmail());
        gwtOrganization.setPhoneNumber(organization.getPhoneNumber());
        gwtOrganization.setAddressLine1(organization.getAddressLine1());
        gwtOrganization.setAddressLine2(organization.getAddressLine2());
        gwtOrganization.setZipPostCode(organization.getZipPostCode());
        gwtOrganization.setCity(organization.getCity());
        gwtOrganization.setStateProvinceCounty(organization.getStateProvinceCounty());
        gwtOrganization.setCountry(organization.getCountry());

        return gwtOrganization;
    }

    public static GwtUser convert(User user)
            throws KapuaException {

        GwtUser gwtUser = new GwtUser();

        gwtUser.setId(user.getId().getShortId());
        gwtUser.setScopeId(user.getScopeId().getShortId());
        gwtUser.setUsername(user.getName());
        gwtUser.setCreatedOn(user.getCreatedOn());
        gwtUser.setModifiedOn(user.getModifiedOn());
        gwtUser.setDisplayName(user.getDisplayName());
        gwtUser.setEmail(user.getEmail());
        gwtUser.setPhoneNumber(user.getPhoneNumber());
        gwtUser.setStatus(user.getStatus().name());
        gwtUser.setCreatedBy(user.getCreatedBy().getShortId());
        gwtUser.setModifiedBy(user.getModifiedBy().getShortId());
        gwtUser.setOptlock(user.getOptlock());
        return gwtUser;
    }

    public static GwtDevice convert(Device device)
            throws KapuaException {
        GwtDevice gwtDevice = new GwtDevice();
        gwtDevice.setId(device.getId().getShortId());
        gwtDevice.setScopeId(device.getScopeId().getShortId());
        gwtDevice.setGwtDeviceStatus(device.getStatus().toString());
        gwtDevice.setClientId(device.getClientId());
        gwtDevice.setDisplayName(device.getDisplayName());
        gwtDevice.setModelId(device.getModelId());
        gwtDevice.setSerialNumber(device.getSerialNumber());
        gwtDevice.setFirmwareVersion(device.getFirmwareVersion());
        gwtDevice.setBiosVersion(device.getBiosVersion());
        gwtDevice.setOsVersion(device.getOsVersion());
        gwtDevice.setJvmVersion(device.getJvmVersion());
        gwtDevice.setOsgiVersion(device.getOsgiFrameworkVersion());
        gwtDevice.setAcceptEncoding(device.getAcceptEncoding());
        gwtDevice.setApplicationIdentifiers(device.getApplicationIdentifiers());
        gwtDevice.setLastEventOn(device.getLastEventOn());

        gwtDevice.setIccid(device.getIccid());
        gwtDevice.setImei(device.getImei());
        gwtDevice.setImsi(device.getImsi());

        String lastEventType = device.getLastEventType() != null ? device.getLastEventType().name() : "";
        gwtDevice.setLastEventType(lastEventType);

        // custom Attributes
        gwtDevice.setCustomAttribute1(device.getCustomAttribute1());
        gwtDevice.setCustomAttribute2(device.getCustomAttribute2());
        gwtDevice.setCustomAttribute3(device.getCustomAttribute3());
        gwtDevice.setCustomAttribute4(device.getCustomAttribute4());
        gwtDevice.setCustomAttribute5(device.getCustomAttribute5());

        gwtDevice.setOptlock(device.getOptlock());

        // Device connection
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        DeviceConnectionFactory deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(device.getScopeId());
        KapuaAndPredicate andPredicate = new AndPredicate();
        andPredicate = andPredicate.and(new AttributePredicate<String>(DeviceConnectionPredicates.CLIENT_ID, device.getClientId()));
        // andPredicate = andPredicate.and(new AttributePredicate<DeviceConnectionStatus[]>(DeviceConnectionPredicates.CONNECTION_STATUS,
        // new DeviceConnectionStatus[] { DeviceConnectionStatus.CONNECTED, DeviceConnectionStatus.MISSING }));

        query.setPredicate(andPredicate);

        KapuaListResult<DeviceConnection> deviceConnections = deviceConnectionService.query(query);

        if (!deviceConnections.isEmpty()) {
            DeviceConnection connection = deviceConnections.getItem(0);

            gwtDevice.setGwtDeviceConnectionStatus(connection.getStatus().toString());
            gwtDevice.setConnectionIp(connection.getClientIp());
            gwtDevice.setDeviceUserId(connection.getUserId().getShortId());
        }
        return gwtDevice;
    }

    public static GwtDeviceEvent convert(DeviceEvent deviceEvent) {
        GwtDeviceEvent gwtDeviceEvent = new GwtDeviceEvent();
        gwtDeviceEvent.setDeviceId(deviceEvent.getDeviceId().getShortId());
        gwtDeviceEvent.setSentOn(deviceEvent.getSentOn());
        gwtDeviceEvent.setReceivedOn(deviceEvent.getReceivedOn());
        gwtDeviceEvent.setEventType(deviceEvent.getResource());
        gwtDeviceEvent.setGwtActionType(deviceEvent.getAction().name());
        gwtDeviceEvent.setGwtResponseCode(deviceEvent.getResponseCode().name());

        String escapedMessage = KapuaSafeHtmlUtils.htmlEscape(deviceEvent.getEventMessage());
        gwtDeviceEvent.setEventMessage(escapedMessage);

        return gwtDeviceEvent;
    }

}
