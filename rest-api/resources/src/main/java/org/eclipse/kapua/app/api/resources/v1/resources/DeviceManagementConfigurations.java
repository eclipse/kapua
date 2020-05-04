/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.device.management.ConfigurationFormat;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfigurationUtils;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.registry.Device;

import org.xml.sax.SAXException;

@Path("{scopeId}/devices/{deviceId}/configurations")
public class DeviceManagementConfigurations extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConfigurationManagementService configurationService = locator.getService(DeviceConfigurationManagementService.class);

    /**
     * Returns the current configuration of the device.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public DeviceConfiguration get(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return getComponent(scopeId, deviceId, null, timeout);
    }

    /**
     * Returns the current configuration of the device.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @param configurationFormat
     *            The format of the configuration. Can be DEVICE or PLATFORM
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @throws JAXBException
     *             Error when serializing the result
     * @since 1.2.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML })
    public String getXml(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("format") @DefaultValue("PLATFORM") ConfigurationFormat configurationFormat,
            @QueryParam("timeout") Long timeout) throws KapuaException, JAXBException {
        DeviceConfiguration deviceConfiguration = configurationService.get(scopeId, deviceId, null, null, timeout);
        if (configurationFormat == ConfigurationFormat.DEVICE) {
            return XmlUtil.marshal(KuraDeviceConfigurationUtils.toKuraConfiguration(deviceConfiguration));
        } else {
            return XmlUtil.marshal(deviceConfiguration);
        }
    }

    /**
     * Updates the configuration of a {@link Device}
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @param deviceConfiguration
     *            The configuration to send to the {@link Device}
     * @return The {@link Response} of the operation
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceConfiguration deviceConfiguration) throws KapuaException {
        configurationService.put(scopeId, deviceId, deviceConfiguration, timeout);

        return returnNoContent();
    }

    /**
     * Updates the configuration of a {@link Device}
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @param deviceConfigurationString
     *            The configuration to send to the {@link Device}
     * @return The {@link Response} of the operation
     * @throws KapuaException
     *            Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @throws JAXBException
     *            Error when serializing the result
     * @throws XMLStreamException
     *            Error when serializing the result
     * @throws FactoryConfigurationError
     *            Error when serializing the result
     * @throws SAXException
     *            Error when serializing the result
     * @since 1.2.0
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_XML })
    public Response updateXml(
            @Context HttpHeaders httpHeaders,
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            String deviceConfigurationString) throws KapuaException, JAXBException, XMLStreamException, FactoryConfigurationError, SAXException {
        DeviceConfiguration deviceConfiguration;
        try {
            KuraDeviceConfiguration kuraDeviceConfiguration = XmlUtil.unmarshal(deviceConfigurationString, KuraDeviceConfiguration.class);
            deviceConfiguration = KuraDeviceConfigurationUtils.toDeviceConfiguration(kuraDeviceConfiguration);
        } catch (UnmarshalException exception) {
            deviceConfiguration = XmlUtil.unmarshal(deviceConfigurationString, DeviceConfiguration.class);
        }

        configurationService.put(scopeId, deviceId, deviceConfiguration, timeout);

        return returnNoContent();
    }

    /**
     * Returns the configuration of a device or the configuration of the OSGi component
     * identified with specified PID (service's persistent identity).
     * In the OSGi framework, the service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param componentId
     *            An optional id of the component to get the configuration for
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{componentId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DeviceConfiguration getComponent(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("componentId") String componentId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return configurationService.get(scopeId, deviceId, null, componentId, timeout);
    }

    /**
     * Returns the configuration of a device or the configuration of the OSGi component
     * identified with specified PID (service's persistent identity).
     * In the OSGi framework, the service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param componentId
     *            An optional id of the component to get the configuration for
     * @param configurationFormat
     *            The format of the configuration. Can be DEVICE or PLATFORM
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @throws JAXBException
     *            Error when serializing the result
     * @since 1.2.0
     */
    @GET
    @Path("{componentId}")
    @Produces({ MediaType.APPLICATION_XML })
    public String getComponentXml(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("componentId") String componentId,
            @QueryParam("format") @DefaultValue("PLATFORM") ConfigurationFormat configurationFormat,
            @QueryParam("timeout") Long timeout) throws KapuaException, JAXBException {
        DeviceConfiguration deviceConfiguration = configurationService.get(scopeId, deviceId, null, componentId, timeout);

        if (configurationFormat == ConfigurationFormat.DEVICE) {
            return XmlUtil.marshal(KuraDeviceConfigurationUtils.toKuraConfiguration(deviceConfiguration));
        } else {
            return XmlUtil.marshal(deviceConfiguration);
        }
    }

}
