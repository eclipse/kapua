/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.Password;
import org.eclipse.kapua.commons.configuration.metatype.TadImpl;
import org.eclipse.kapua.commons.configuration.metatype.TiconImpl;
import org.eclipse.kapua.commons.configuration.metatype.TocdImpl;
import org.eclipse.kapua.commons.configuration.metatype.ToptionImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;

public class KuraDeviceConfigurationUtils {

    private KuraDeviceConfigurationUtils() { }

    //
    // Kura to Kapua
    public static KuraDeviceConfiguration toKuraConfiguration(DeviceConfiguration kapuaDeviceConfiguration) throws KapuaException {
        return translateKapuaConfiguration(kapuaDeviceConfiguration);
    }

    private static KuraDeviceConfiguration translateKapuaConfiguration(DeviceConfiguration kapuaDeviceConfiguration)
            throws KapuaException {
        KuraDeviceConfiguration kuraDeviceConfiguration = new KuraDeviceConfiguration();

        for (DeviceComponentConfiguration kapuaDeviceCompConf : kapuaDeviceConfiguration.getComponentConfigurations()) {

            KuraDeviceComponentConfiguration kuraComponentConfiguration = new KuraDeviceComponentConfiguration();
            kuraComponentConfiguration.setComponentId(kapuaDeviceCompConf.getId());
            kuraComponentConfiguration.setProperties(translateKapuaComponentConfigurationProperties(kapuaDeviceCompConf.getProperties()));

            // Translate also definitions when they are available
            if (kapuaDeviceCompConf.getDefinition() != null) {
                kuraComponentConfiguration.setDefinition(translateKapuaDefinition(kapuaDeviceCompConf.getDefinition()));
            }

            // Add to kapua configuration
            kuraDeviceConfiguration.getConfigurations().add(kuraComponentConfiguration);
        }

        return kuraDeviceConfiguration;
    }

    private static KapuaTocd translateKapuaDefinition(KapuaTocd kapuaDefinition) {
        TocdImpl definition = new TocdImpl();

        definition.setId(kapuaDefinition.getId());
        definition.setName(kapuaDefinition.getName());
        definition.setDescription(kapuaDefinition.getDescription());

        kapuaDefinition.getAD().forEach(kapuaAd -> {
            TadImpl ad = new TadImpl();
            ad.setCardinality(kapuaAd.getCardinality());
            ad.setDefault(ad.getDefault());
            ad.setDescription(kapuaAd.getDescription());
            ad.setId(kapuaAd.getId());
            ad.setMax(kapuaAd.getMax());
            ad.setMin(kapuaAd.getMin());
            ad.setName(kapuaAd.getName());
            ad.setType(kapuaAd.getType());
            ad.setRequired(kapuaAd.isRequired());

            kapuaAd.getOption().forEach(kuraToption -> {
                ToptionImpl kapuaToption = new ToptionImpl();
                kapuaToption.setLabel(kuraToption.getLabel());
                kapuaToption.setValue(kuraToption.getValue());
                ad.addOption(kapuaToption);
            });

            kapuaAd.getOtherAttributes().forEach(ad::putOtherAttribute); // Such magic!

            definition.addAD(ad);
        });

        kapuaDefinition.getIcon().forEach(kapuaIcon -> {
            KapuaTicon icon = new TiconImpl();
            icon.setResource(kapuaIcon.getResource());
            icon.setSize(kapuaIcon.getSize());

            definition.addIcon(icon);
        });

        kapuaDefinition.getAny().forEach(definition::addAny); // Such magic!
        kapuaDefinition.getOtherAttributes().forEach(definition::putOtherAttribute); // Such magic!

        return definition;
    }

    private static Map<String, Object> translateKapuaComponentConfigurationProperties(Map<String, Object> kapuaProperties) {
        Map<String, Object> properties = new HashMap<>();

        kapuaProperties.forEach((key, value) -> {
            // Special management of Password type field
            if (value instanceof Password) {
                value = new KuraPassword(((Password) value).getPassword());
            } else if (value instanceof Password[]) {
                Password[] passwords = (Password[]) value;
                KuraPassword[] kuraPasswords = new KuraPassword[passwords.length];

                int i = 0;
                for (Password p : passwords) {
                    kuraPasswords[i++] = new KuraPassword(p.getPassword());
                }

                value = kuraPasswords;
            }

            // Set property
            properties.put(key, value);
        });

        return properties;
    }

    //
    // Kapua to Kura
    public static DeviceConfiguration toDeviceConfiguration(KuraDeviceConfiguration kuraDeviceConfiguration) throws KapuaException {
        DeviceConfigurationFactory deviceConfigurationFactory = KapuaLocator.getInstance().getFactory(DeviceConfigurationFactory.class);
        DeviceConfiguration deviceConfiguration = deviceConfigurationFactory.newConfigurationInstance();
        for (KuraDeviceComponentConfiguration kuraDeviceCompConf : kuraDeviceConfiguration.getConfigurations()) {

            String componentId = kuraDeviceCompConf.getComponentId();
            DeviceComponentConfiguration deviceComponentConfiguration = deviceConfigurationFactory.newComponentConfigurationInstance(componentId);
            deviceComponentConfiguration.setProperties(translateKuraComponentConfigurationProperties(kuraDeviceCompConf.getProperties()));

            // Translate also definitions when they are available
            if (kuraDeviceCompConf.getDefinition() != null) {
                deviceComponentConfiguration.setDefinition(translateKuraDefinition(kuraDeviceCompConf.getDefinition()));
            }

            // Add to kapua configuration
            deviceConfiguration.getComponentConfigurations().add(deviceComponentConfiguration);
        }
        return deviceConfiguration;
    }

    private static KapuaTocd translateKuraDefinition(KapuaTocd kuraDefinition) {
        TocdImpl definition = new TocdImpl();

        definition.setId(kuraDefinition.getId());
        definition.setName(kuraDefinition.getName());
        definition.setDescription(kuraDefinition.getDescription());

        kuraDefinition.getAD().forEach(kuraAd -> {
            TadImpl ad = new TadImpl();
            ad.setCardinality(kuraAd.getCardinality());
            ad.setDefault(ad.getDefault());
            ad.setDescription(kuraAd.getDescription());
            ad.setId(kuraAd.getId());
            ad.setMax(kuraAd.getMax());
            ad.setMin(kuraAd.getMin());
            ad.setName(kuraAd.getName());
            ad.setType(kuraAd.getType());
            ad.setRequired(kuraAd.isRequired());

            kuraAd.getOption().forEach(kuraToption -> {
                ToptionImpl kapuaToption = new ToptionImpl();
                kapuaToption.setLabel(kuraToption.getLabel());
                kapuaToption.setValue(kuraToption.getValue());
                ad.addOption(kapuaToption);
            });

            kuraAd.getOtherAttributes().forEach(ad::putOtherAttribute);
            definition.addAD(ad);
        });

        kuraDefinition.getIcon().forEach(kuraIcon -> {
            KapuaTicon icon = new TiconImpl();
            icon.setResource(kuraIcon.getResource());
            icon.setSize(kuraIcon.getSize());
            definition.addIcon(icon);
        });

        kuraDefinition.getAny().forEach(definition::addAny);
        kuraDefinition.getOtherAttributes().forEach(definition::putOtherAttribute);

        return definition;
    }

    private static Map<String, Object> translateKuraComponentConfigurationProperties(Map<String, Object> kuraProperties) {
        Map<String, Object> properties = new HashMap<>();

        kuraProperties.forEach((key, value) -> {

            //
            // Special management of Password type field
            if (value instanceof KuraPassword) {
                value = new Password(((KuraPassword) value).getPassword());
            } else if (value instanceof KuraPassword[]) {
                KuraPassword[] kuraPasswords = (KuraPassword[]) value;
                Password[] passwords = new Password[kuraPasswords.length];

                int i = 0;
                for (KuraPassword p : kuraPasswords) {
                    passwords[i++] = new Password(p.getPassword());
                }

                value = passwords;
            }

            //
            // Set property
            properties.put(key, value);
        });
        return properties;
    }
}
