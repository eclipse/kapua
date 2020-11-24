/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.service.internal.KapuaServiceDisabledException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.commons.util.StringUtil;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;

import org.xml.sax.SAXException;

/**
 * Configurable service definition abstract reference implementation.
 */
public abstract class AbstractKapuaConfigurableService extends AbstractKapuaService implements KapuaConfigurableService {

    private Domain domain;
    private String pid;
    private static final int SIZEMAX =
            SystemSetting.getInstance().getInt(SystemSettingKey.TMETADATA_LOCAL_CACHE_SIZE_MAXIMUM, 100);
    private static final EntityCache PRIVATE_ENTITY_CACHE =
            AbstractKapuaConfigurableServiceCache.getInstance().createCache();
    /**
     * This cache is to hold the KapuaTocd that are read from the metatype files.
     * The key is a {@link Triple} composed by:
     * <ul>
     *     <li>The service PID</li>
     *     <li>The ID of the {@link Account} for the current request</li>
     *     <li>A {@link Boolean} flag indicating whether disabled properties are excluded from the AD or not</li>
     * </ul>
     */
    private static final LocalCache<Triple<String, KapuaId, Boolean>, KapuaTocd> KAPUA_TOCD_LOCAL_CACHE =
            new LocalCache<>(SIZEMAX, null);
    /**
     * This cache only holds the {@link Boolean} value {@literal True} if the OCD has been already read from the file
     * at least once, regardless of the value. With this we can know when a read from {@code KAPUA_TOCD_LOCAL_CACHE}
     * returns {@literal null} becauseof the requested key is not present, and when the key is present but its actual value
     * is {@literal null}.
     */
    private static final LocalCache<Triple<String, KapuaId, Boolean>, Boolean> KAPUA_TOCD_EMPTY_LOCAL_CACHE =
            new LocalCache<>(SIZEMAX, false);

    protected AbstractKapuaConfigurableService(String pid, Domain domain, EntityManagerFactory entityManagerFactory) {
        this(pid, domain, entityManagerFactory, null);
    }

    protected AbstractKapuaConfigurableService(String pid, Domain domain, EntityManagerFactory entityManagerFactory, AbstractEntityCacheFactory abstractCacheFactory) {
        super(entityManagerFactory, abstractCacheFactory);
        this.pid = pid;
        this.domain = domain;
    }

    /**
     * Reads metadata for the service pid
     *
     * @param pid the persistent ID of the service
     * @return the metadata
     * @throws Exception In case of an error
     */
    private static KapuaTmetadata readMetadata(String pid) throws JAXBException, SAXException, XMLStreamException, IOException {
        URL url = ResourceUtils.getResource(String.format("META-INF/metatypes/%s.xml", pid));

        if (url == null) {
            return null;
        }

        return XmlUtil.unmarshal(ResourceUtils.openAsReader(url, StandardCharsets.UTF_8), KapuaTmetadata.class);
    }

    /**
     * Validate configuration
     *
     * @param ocd
     * @param updatedProps
     * @param scopeId
     * @param parentId
     * @throws KapuaException
     */
    private void validateConfigurations(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId)
            throws KapuaException {
        if (ocd != null) {

            // Get Disabled Properties
            List<KapuaTad> disabledProperties = ocd.getAD().stream().filter(ad -> !isPropertyEnabled(ad, scopeId)).collect(Collectors.toList());

            if (!disabledProperties.isEmpty()) {
                // If there's any disabled property, read current values to overwrite the proposed ones
                Map<String, Object> originalValues = getConfigValues(scopeId, false);
                if (originalValues != null) {
                    disabledProperties.forEach(disabledProp -> updatedProps.put(disabledProp.getId(), originalValues.get(disabledProp.getId())));
                }
            }

            // build a map of all the attribute definitions
            Map<String, KapuaTad> attrDefs = ocd.getAD().stream().collect(Collectors.toMap(KapuaTad::getId, ad -> ad));

            // loop over the proposed property values
            // and validate them against the definition
            for (Entry<String, Object> property : updatedProps.entrySet()) {

                String key = property.getKey();
                KapuaTad attrDef = attrDefs.get(key);

                // is attribute undefined?
                if (attrDef == null) {
                    // we do not have an attribute descriptor to the validation
                    // against
                    // As OSGI insert attributes at runtime like service.pid,
                    // component.name,
                    // for the attribute for which we do not have a definition,
                    // just accept them.
                    continue;
                }

                // validate the attribute value
                Object objectValue = property.getValue();
                String stringValue = StringUtil.valueToString(objectValue);
                if (stringValue != null) {
                    ValueTokenizer tokenizer = new ValueTokenizer(stringValue);
                    String result = tokenizer.validate(attrDef);
                    if (result != null && !result.isEmpty()) {
                        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ATTRIBUTE_INVALID, attrDef.getId() + ": " + result);
                    }
                }
            }

            checkRequiredProperties(ocd, updatedProps);

            validateNewConfigValuesCoherence(ocd, updatedProps, scopeId, parentId);
        }
    }

    private void checkRequiredProperties(KapuaTocd ocd, Map<String, Object> updatedProps) throws KapuaConfigurationException {
        // make sure all required properties are set
        for (KapuaTad attrDef : ocd.getAD()) {
            // to the required attributes make sure a value is defined.
            if (Boolean.TRUE.equals(attrDef.isRequired()) && updatedProps.get(attrDef.getId()) == null) {
                // if the default one is not defined, throw
                // exception.
                throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.REQUIRED_ATTRIBUTE_MISSING, attrDef.getId());
            }
        }
    }

    /**
     * Validates that the configurations is coherent. By default returns true, but an extending {@link org.eclipse.kapua.service.KapuaService}
     * may have its own logic
     *
     * @param ocd          The {@link KapuaTocd} containing the definition of the service configurations
     * @param updatedProps A {@link Map} containing the new values for the service
     * @param scopeId      The Scope ID of the current configuration
     * @param parentId     The ID of the Parent Scope
     * @return {@literal true} if the configuration is valid, {@literal false} otherwise
     * @throws KapuaException When something goes wrong
     */
    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException {
        return true;
    }

    /**
     * Convert the properties map to {@link Properties}
     *
     * @param values
     * @return
     */
    private static Properties toProperties(Map<String, Object> values) {
        Properties props = new Properties();
        for (Entry<String, Object> entry : values.entrySet()) {
            if (entry.getValue() != null) {
                props.setProperty(entry.getKey(), StringUtil.valueToString(entry.getValue()));
            }
        }

        return props;
    }

    /**
     * Convert the {@link Properties} to a properties map
     *
     * @param ocd
     * @param props
     * @return
     * @throws KapuaException
     */
    protected static Map<String, Object> toValues(@NotNull KapuaTocd ocd, Properties props) throws KapuaException {
        List<KapuaTad> ads = ocd.getAD();
        Map<String, Object> values = new HashMap<>();
        for (KapuaTad ad : ads) {
            String valueStr = props == null ? ad.getDefault() : props.getProperty(ad.getId(), ad.getDefault());
            Object value = StringUtil.stringToValue(ad.getType().value(), valueStr);
            values.put(ad.getId(), value);
        }

        return values;
    }

    /**
     * Create the service configuration entity
     *
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig createConfig(ServiceConfig serviceConfig)
            throws KapuaException {

        return entityManagerSession.doTransactedAction(EntityManagerContainer.<ServiceConfig>create().onResultHandler(em -> ServiceDAO.create(em, serviceConfig))
                .onBeforeHandler(() -> {
                    PRIVATE_ENTITY_CACHE.removeList(serviceConfig.getScopeId(), pid);
                    return null;
                }));
    }

    /**
     * Update the service configuration entity
     *
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig updateConfig(ServiceConfig serviceConfig)
            throws KapuaException {
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<ServiceConfig>create().onResultHandler(em -> {
            ServiceConfig oldServiceConfig = ServiceConfigDAO.find(em, serviceConfig.getScopeId(), serviceConfig.getId());
            if (oldServiceConfig == null) {
                throw new KapuaEntityNotFoundException(ServiceConfig.TYPE, serviceConfig.getId());
            }

            if (!Objects.equals(oldServiceConfig.getScopeId(), serviceConfig.getScopeId())) {
                throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, null, "scopeId");
            }
            if (!oldServiceConfig.getPid().equals(serviceConfig.getPid())) {
                throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, null, "pid");
            }

            // Update
            return ServiceConfigDAO.update(em, serviceConfig);
        }).onBeforeHandler(() -> {
            PRIVATE_ENTITY_CACHE.removeList(serviceConfig.getScopeId(), pid);
            return null;
        }));
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return getConfigMetadata(scopeId, true);
    }

    protected KapuaTocd getConfigMetadata(KapuaId scopeId, boolean excludeDisabled) throws KapuaException {
        if (!isServiceEnabled(scopeId)) {
            throw new KapuaServiceDisabledException(pid);
        }
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));
        // Keep distinct values for service PID, Scope ID and disabled properties included/excluded from AD
        Triple<String, KapuaId, Boolean> cacheKey = Triple.of(pid, scopeId, excludeDisabled);
        try {
            // Check if the OCD is already in cache, but not in the "empty" cache
            KapuaTocd tocd = KAPUA_TOCD_LOCAL_CACHE.get(cacheKey);
            if (tocd == null && !KAPUA_TOCD_EMPTY_LOCAL_CACHE.get(cacheKey)) {
                // If not, read metadata and process it
                tocd = processMetadata(readMetadata(pid), scopeId, excludeDisabled);
                // If null, put it in the "empty" ocd cache, else put it in the "standard" cache
                if (tocd != null) {
                    // If the value is not null, put it in "standard" cache and remove the entry from the "empty" cache if present
                    KAPUA_TOCD_LOCAL_CACHE.put(cacheKey, tocd);
                    KAPUA_TOCD_EMPTY_LOCAL_CACHE.remove(cacheKey);
                } else {
                    // If the value is null, just remember we already read it from file at least once
                    KAPUA_TOCD_EMPTY_LOCAL_CACHE.put(cacheKey, true);
                }
            }
            return tocd;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    /**
     * Process metadata to exclude disabled services and properties
     *
     * @param metadata        A {@link KapuaTmetadata} object
     * @param excludeDisabled if {@literal true} exclude disabled properties from the AD object
     * @return The processed {@link KapuaTocd} object
     */
    private KapuaTocd processMetadata(KapuaTmetadata metadata, KapuaId scopeId, boolean excludeDisabled) {
        if (metadata != null && metadata.getOCD() != null && !metadata.getOCD().isEmpty()) {
            for (KapuaTocd ocd : metadata.getOCD()) {
                if (ocd.getId() != null && ocd.getId().equals(pid) && isServiceEnabled(scopeId)) {
                    ocd.getAD().removeIf(ad -> excludeDisabled && !isPropertyEnabled(ad, scopeId));
                    return ocd;
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return getConfigValues(scopeId, true);
    }

    protected Map<String, Object> getConfigValues(KapuaId scopeId, boolean excludeDisabled) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);

        AndPredicate predicate = query.andPredicate(
                query.attributePredicate(ServiceConfigAttributes.SERVICE_ID, pid, Operator.EQUAL),
                query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, scopeId, Operator.EQUAL)
        );

        query.setPredicate(predicate);

        ServiceConfigListResult result = entityManagerSession.doAction(EntityManagerContainer.<ServiceConfigListResult>create().onResultHandler(em -> ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query))
                .onBeforeHandler(() -> (ServiceConfigListResult) PRIVATE_ENTITY_CACHE.getList(scopeId, pid))
                .onAfterHandler(entity -> PRIVATE_ENTITY_CACHE.putList(scopeId, pid, entity)));

        Properties properties = null;
        if (result != null && !result.isEmpty()) {
            properties = result.getFirstItem().getConfigurations();
        }

        KapuaTocd ocd = getConfigMetadata(scopeId, excludeDisabled);
        return ocd == null ? null : toValues(ocd, properties);
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        KapuaTocd ocd = getConfigMetadata(scopeId, false);

        UserService userService = locator.getService(UserService.class);
        String rootUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        User rootUser = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(rootUserName));

        Map<String, Object> originalValues = getConfigValues(scopeId);

        for (KapuaTad ad : ocd.getAD()) {
            boolean allowSelfEdit = Boolean.parseBoolean(ad.getOtherAttributes().getOrDefault(new QName("allowSelfEdit"), "false"));

            boolean preventChange =
                    // if current user is not root user...
                    !KapuaSecurityUtils.getSession().getUserId().equals(rootUser.getId()) &&
                    // current configuration does not allow self edit...
                    !allowSelfEdit &&
                    // a configuration for the current logged account is about to be changed...
                    KapuaSecurityUtils.getSession().getScopeId().equals(scopeId) &&
                    // and the new value is different from the other one...
                    !originalValues.get(ad.getId()).equals(values.get(ad.getId()));

            if (preventChange) {
                // ... prevent the change!
                throw KapuaException.internalError(String.format("The configuration \"%s\" cannot be changed from an user of the account", ad.getId()));
            }
        }

        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.write, scopeId));

        validateConfigurations(ocd, values, scopeId, parentId);

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(ServiceConfigAttributes.SERVICE_ID, pid, Operator.EQUAL),
                        query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, scopeId, Operator.EQUAL)
                )
        );

        ServiceConfigListResult result = entityManagerSession.doAction(EntityManagerContainer.<ServiceConfigListResult>create().onResultHandler(em -> ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query)));

        Properties props = toProperties(values);
        if (result == null || result.isEmpty()) {
            // In not exists create then return
            ServiceConfig serviceConfigNew = new ServiceConfigImpl(scopeId);
            serviceConfigNew.setPid(pid);
            serviceConfigNew.setConfigurations(props);

            createConfig(serviceConfigNew);
        } else {
            // If exists update it
            ServiceConfig serviceConfig = result.getFirstItem();
            serviceConfig.setConfigurations(props);

            updateConfig(serviceConfig);
        }
    }

    protected boolean isPropertyEnabled(KapuaTad ad, KapuaId scopeId) {
        return true;
    }

}
