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
package org.eclipse.kapua.locator.guice;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;

public class LocatorConfig {
	
	private static final String SERVICE_RESOURCE_INTERFACES = "provided.api";
	private static final String SERVICE_RESOURCE_PACKAGES = "packages.package";
	
	private final URL url;
	private List<String> packageNames;
	private List<String> providedInterfaceNames;
	
	private LocatorConfig(URL url)
	{
		this.url = url;
		this.packageNames = new ArrayList<String>();
		this.providedInterfaceNames = new ArrayList<String>();
	}
	
	public static LocatorConfig fromURL(URL url) throws KapuaLocatorException 
	{

		if (url == null)
			throw new IllegalArgumentException();
				
		LocatorConfig config = new LocatorConfig(url);
		
		XMLConfiguration xmlConfig;
		try {
			xmlConfig = new XMLConfiguration(url);
		} catch (ConfigurationException e) {
			throw new KapuaLocatorException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
		}
		
		Object props = xmlConfig.getProperty(SERVICE_RESOURCE_PACKAGES);
		if (props instanceof Collection)
			config.packageNames.addAll((Collection<String>) props);
		if (props instanceof String)
			config.packageNames.add((String) props);
		
		props = xmlConfig.getProperty(SERVICE_RESOURCE_INTERFACES);
		if (props instanceof Collection)
			config.providedInterfaceNames.addAll((Collection<String>) props);
		if (props instanceof String)
			config.providedInterfaceNames.add((String)props);
		
		return config;
	}

	public URL getURL() {
		return url;
	}

	public Collection<String> getPackageNames() {
		return Collections.unmodifiableCollection(packageNames);
	}

	public Collection<String> getProvidedInterfaceNames() {
		return Collections.unmodifiableCollection(providedInterfaceNames);
	}
}
