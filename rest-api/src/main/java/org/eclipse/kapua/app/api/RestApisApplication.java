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
 *******************************************************************************/
package org.eclipse.kapua.app.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.UriConnegFilter;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.xml.sax.SAXException;

import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

public class RestApisApplication extends ResourceConfig {

	public RestApisApplication() throws JAXBException {
		packages("org.eclipse.kapua.app.api", "org.eclipse.kapua.service.account", "org.eclipse.kapua.service.account.internal",
		         "org.eclipse.kapua.service.user", "org.eclipse.kapua.service.user.internal");

		// Bind media type to resource extension
		HashMap<String, MediaType> mappedMediaTypes = new HashMap<String, MediaType>();
		mappedMediaTypes.put("xml", MediaType.APPLICATION_XML_TYPE);
		mappedMediaTypes.put("json", MediaType.APPLICATION_JSON_TYPE);

		property(ServerProperties.MEDIA_TYPE_MAPPINGS, mappedMediaTypes);
		register(UriConnegFilter.class);
		register(JaxbContextResolver.class);
		register(RestApiJAXBContextProvider.class);
		register(KapuaSerializableBodyWriter.class);
		register(ListBodyWriter.class);
		
		
		
		
		
		// Hook the swagger-ui
//		registerClasses(ApiListingResource.class,
//						SwaggerSerializers.class);

		register(new ContainerLifecycleListener() {

			@Override
			public void onStartup(Container container) {
				ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();

				RestApiJAXBContextProvider provider = serviceLocator.createAndInitialize(RestApiJAXBContextProvider.class);
 				XmlUtil.setContextProvider(provider);
			}

			@Override
			public void onReload(Container container) {
				//Nothing todo
			}

			@Override
			public void onShutdown(Container container) {
				//Nothing todo
			}

		});
		
//		Role ri = new RoleImpl((KapuaId)null);
//        RolePermission rpi = new RolePermissionImpl(null, new PermissionImpl("asd", Actions.connect, null));
//        Set<RolePermission> set = new HashSet<>();
//        set.add(rpi);
//        ri.setRolePermissions(set);
//        String marshalled = XmlUtil.marshal(ri);
//        try {
//            Role ri2 = XmlUtil.unmarshal(marshalled, Role.class);
//        } catch (XMLStreamException | FactoryConfigurationError | SAXException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("");
	}
}
