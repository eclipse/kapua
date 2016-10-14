/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.internal;

import java.util.Date;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestChannel;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestPayload;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponseMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponsePayload;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

public class DeviceCommandManagementServiceImpl implements DeviceCommandManagementService
{
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public DeviceCommandOutput exec(KapuaId scopeId, KapuaId deviceId, DeviceCommandInput commandInput, Long timeout)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(commandInput, "commandInput");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementDomain.DEVICE_MANAGEMENT, Actions.execute, scopeId));

        //
        // Prepare the request
        CommandRequestChannel commandRequestChannel = new CommandRequestChannel();
        commandRequestChannel.setAppName(CommandAppProperties.APP_NAME);
        commandRequestChannel.setVersion(CommandAppProperties.APP_VERSION);
        commandRequestChannel.setMethod(KapuaMethod.EXECUTE);

        CommandRequestPayload commandRequestPayload = new CommandRequestPayload();
        commandRequestPayload.setCommand(commandInput.getCommand());
        commandRequestPayload.setArguments(commandInput.getArguments());
        commandRequestPayload.setStdin(commandInput.getStdin());
        commandRequestPayload.setTimeout(commandInput.getTimeout());
        commandRequestPayload.setWorkingDir(commandInput.getWorkingDir());
        commandRequestPayload.setEnvironmentPairs(commandInput.getEnvironment());
        commandRequestPayload.setRunAsync(commandInput.isRunAsynch());
        commandRequestPayload.setPassword(commandInput.getPassword());
        commandRequestPayload.setBody(commandInput.getBody());

        CommandRequestMessage commandRequestMessage = new CommandRequestMessage();
        commandRequestMessage.setScopeId(scopeId);
        commandRequestMessage.setDeviceId(deviceId);
        commandRequestMessage.setCapturedOn(new Date());
        commandRequestMessage.setPayload(commandRequestPayload);
        commandRequestMessage.setChannel(commandRequestChannel);

        //
        // Do exec
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(commandRequestMessage, timeout);
        CommandResponseMessage responseMessage = (CommandResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), CommandAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.EXECUTE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        //
        // Parse the response
        CommandResponsePayload responsePayload = responseMessage.getPayload();

        DeviceCommandOutput deviceCommandOutput = new DeviceCommandOutputImpl();
        deviceCommandOutput.setExceptionMessage(responsePayload.getExceptionMessage());
        deviceCommandOutput.setExceptionStack(responsePayload.getExceptionStack());
        deviceCommandOutput.setExitCode(responsePayload.getExitCode());
        deviceCommandOutput.setHasTimedout(false); // FIXME: implement track of timeout!!!
        deviceCommandOutput.setStderr(responsePayload.getStderr());
        deviceCommandOutput.setStdout(responsePayload.getStdout());

        return deviceCommandOutput;
    }
}
