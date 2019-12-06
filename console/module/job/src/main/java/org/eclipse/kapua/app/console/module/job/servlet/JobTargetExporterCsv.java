/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.targets.JobTarget;

import com.opencsv.CSVWriter;

public class JobTargetExporterCsv extends JobTargetExporter {

    private String scopeId;
    private String jobId;
    private DateFormat dateFormat;
    private CSVWriter writer;

    public JobTargetExporterCsv(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(final String scopeId, String jobId)
            throws ServletException, IOException, KapuaException {
        this.scopeId = scopeId;
        this.jobId = jobId;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(jobId, "UTF-8") + "_job_targets.csv");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Charset.forName("UTF-8"));
        writer = new CSVWriter(osw);

        List<String> cols = new ArrayList<String>();
        Collections.addAll(cols, JOB_TARGET_PROPERTIES);
        writer.writeNext(cols.toArray(new String[] {}));
    }

    @Override
    public void append(KapuaListResult<JobTarget> jobTargets)
            throws ServletException, IOException, KapuaException {

        KapuaLocator locator = KapuaLocator.getInstance();
        final KapuaIdFactory kapuaIdFactory = locator.getFactory(KapuaIdFactory.class);
        final DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        final DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);
        final DeviceQuery deviceQuery = deviceFactory.newQuery(kapuaIdFactory.newKapuaId(scopeId));

        KapuaId[] targetIds = new KapuaId[jobTargets.getSize()];
        int i = 0;
        for (JobTarget jobTarget : jobTargets.getItems()) {
            targetIds[i] = jobTarget.getJobTargetId();
            i++;
        }
        deviceQuery.setPredicate(deviceQuery.attributePredicate(DeviceAttributes.ENTITY_ID, targetIds));
        DeviceListResult deviceListResult = deviceRegistryService.query(deviceQuery);
        Map<String, String> targetClientIdMapping = new HashMap<String, String>();
        Map<String, String> targetDisplayNameMapping = new HashMap<String, String>();
        for (Device device : deviceListResult.getItems()) {
            targetClientIdMapping.put(device.getId().toCompactId(), device.getClientId());
            targetDisplayNameMapping.put(device.getId().toCompactId(), device.getDisplayName());
        }
        for (final JobTarget jobTarget : jobTargets.getItems()) {

            List<String> cols = new ArrayList<String>();
            String deviceId = jobTarget.getJobTargetId().toCompactId();

            // Job Target ID
            cols.add(jobTarget.getId().toCompactId());

            // Job ID
            cols.add(jobId);

            // Device ID
            cols.add(deviceId);

            // Step Index
            cols.add(Integer.toString(jobTarget.getStepIndex()));

            // Status
            cols.add(jobTarget.getStatus().name());

            // Status Message
            cols.add(jobTarget.getStatusMessage());

            // Client ID
            String clientId = targetClientIdMapping.get(deviceId);
            cols.add(clientId != null ? clientId : BLANK);

            // Sent on
            String displayName = targetDisplayNameMapping.get(deviceId);
            cols.add(displayName != null ? displayName : BLANK);

            writer.writeNext(cols.toArray(new String[] {}));
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        writer.flush();
        writer.close();
    }
}
