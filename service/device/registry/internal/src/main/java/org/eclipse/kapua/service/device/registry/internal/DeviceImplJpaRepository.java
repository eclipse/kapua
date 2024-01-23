/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventImpl_;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Optional;

public class DeviceImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<Device, DeviceImpl, DeviceListResult>
        implements DeviceRepository {
    public DeviceImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(DeviceImpl.class, Device.TYPE, () -> new DeviceListResultImpl(), jpaRepoConfig);
    }

    @Override
    public Optional<Device> findByClientId(TxContext tx, KapuaId scopeId, String clientId) throws KapuaException {
        DeviceQueryImpl query = new DeviceQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceAttributes.CLIENT_ID, clientId));
        query.setFetchAttributes(Lists.newArrayList(DeviceAttributes.CONNECTION, DeviceAttributes.LAST_EVENT));
        return Optional.ofNullable(this.query(tx, query).getFirstItem());
    }

    @Override
    public void updateLastEvent(TxContext tx, KapuaId scopeId, KapuaId deviceId, KapuaId deviceEventId, Date receivedOn) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaUpdate<DeviceImpl> update = cb.createCriteriaUpdate(DeviceImpl.class);
        final Root<DeviceImpl> root = update.from(DeviceImpl.class);
        final Join<DeviceImpl, DeviceEventImpl> lastEventJoin = root.join(DeviceImpl_.lastEvent, JoinType.LEFT);
        update.set(root.get(DeviceImpl_.lastEventId), KapuaEid.parseKapuaId(deviceEventId));
        update.set(root.get(DeviceImpl_.modifiedBy), KapuaEid.parseKapuaId(KapuaSecurityUtils.getSession().getUserId()));
        update.set(root.get(DeviceImpl_.modifiedOn), new Date());
        update.where(
                cb.and(
                        cb.equal(root.get(DeviceImpl_.scopeId), KapuaEid.parseKapuaId(scopeId)),
                        cb.equal(root.get(DeviceImpl_.id), KapuaEid.parseKapuaId(deviceId)),
                        cb.or(
                                cb.isNull(lastEventJoin.get(DeviceEventImpl_.receivedOn)),
                                cb.lessThan(lastEventJoin.get(DeviceEventImpl_.receivedOn), receivedOn)
                        )
                )
        );
        em.createQuery(update).executeUpdate();
    }
}
