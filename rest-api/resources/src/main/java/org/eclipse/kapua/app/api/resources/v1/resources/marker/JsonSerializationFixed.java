/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.resources.v1.resources.marker;

import org.eclipse.kapua.app.api.resources.v1.resources.DataMessages;
import org.eclipse.kapua.app.api.core.model.data.JsonDatastoreMessage;
import org.eclipse.kapua.app.api.core.model.data.JsonKapuaDataMessage;
import org.eclipse.kapua.app.api.core.model.device.management.JsonGenericRequestMessage;
import org.eclipse.kapua.app.api.core.model.device.management.JsonGenericResponseMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

/**
 * This is a marker interface to mark JAX-RS resources which do not map to any actual Kapua resource but they wrap existing resources.
 * <p>
 * Any JAX-RS resource marked with this {@code interface} that acts as a bridge for the JSON serialization of generic request objects.
 * <p>
 * The {@link org.eclipse.kapua.message.KapuaPayload} is marshalled with the following format:
 * <pre>
 * &lt;payload xsi:type=&quot;kapuaDataPayload&quot;&gt;
 *     &lt;metrics&gt;
 *         &lt;metric&gt;
 *              &lt;valueType&gt;float&lt;valueType/&gt;
 *              &lt;value&gt;5.0&lt;value/&gt;
 *              &lt;name&gt;temperatureExternal&lt;name/&gt;
 *         &lt;metric/&gt;
 *         &lt;metric&gt;
 *              &lt;valueType&gt;float&lt;valueType/&gt;
 *              &lt;value&gt;19.25&lt;value/&gt;
 *              &lt;name&gt;temperatureInternal&lt;name/&gt;
 *         &lt;metric/&gt;
 *         &lt;metric&gt;
 *              &lt;valueType&gt;float&lt;valueType/&gt;
 *              &lt;value&gt;30.00&lt;value/&gt;
 *              &lt;name&gt;temperatureExhaust&lt;name/&gt;
 *         &lt;metric/&gt;
 *         &lt;metric&gt;
 *              &lt;valueType&gt;integer&lt;valueType/&gt;
 *              &lt;value&gt;-1422687692&lt;value/&gt;
 *              &lt;name&gt;errorCode&lt;name/&gt;
 *         &lt;metric/&gt;
 *     &lt;metrics/&gt;
 *     &lt;body&gt;YXNk&lt;/body&gt;
 * &lt;/payload&gt;
 * </pre>
 * <p>
 * But the JSON has the following format:
 * <pre>
 * "payload": {
 *    "metrics": {
 *       "metric": [
 *           {
 *              "valueType" : "float",
 *              "value" : "5.0",
 *              "name" : "temperatureExternal"
 *           }, {
 *              "valueType" : "float",
 *              "value" : "19.25",
 *              "name" : "temperatureInternal"
 *           }, {
 *              "valueType" : "float",
 *              "value" : "30.0",
 *              "name" : "temperatureExhaust"
 *           }, {
 *              "valueType" : "integer",
 *              "value" : "-1422687692",
 *              "name" : "errorCode"
 *           }
 *       ]
 *    },
 *    "body": "YXNk"
 * }
 * </pre>
 * For some reasons in JSON format "metrics" is an object with "metric" array as a field.
 * <p>
 * Since we weren't able to fina JAXB mapping configuration to allow correct formatting of both XML and JSON,
 * this "bridge" class is introduced to allow a translation of the {@link org.eclipse.kapua.message.KapuaPayload}.
 * <p>
 * This resources then maps to the {@link DataMessages} class, just translating:
 * <ul>
 * <li>{@link KapuaDataMessage} to {@link JsonKapuaDataMessage}</li>
 * <li>{@link DatastoreMessage} to {@link JsonDatastoreMessage}</li>
 * <li>{@link GenericRequestMessage} to {@link JsonGenericRequestMessage}</li>
 * <li>{@link GenericResponseMessage} to {@link JsonGenericResponseMessage}</li>
 * </ul>
 * Final JSON output is:
 * <pre>
 * "payload": {
 *     "metrics": [
 *         {
 *             "valueType" : "float",
 *             "value" : "5.0",
 *             "name" : "temperatureExternal"
 *         }, {
 *             "valueType" : "float",
 *              "value" : "19.25",
 *              "name" : "temperatureInternal"
 *         }, {
 *              "valueType" : "float",
 *              "value" : "30.0",
 *              "name" : "temperatureExhaust"
 *         }, {
 *              "valueType" : "integer",
 *              "value" : "-1422687692",
 *              "name" : "errorCode"
 *         }
 *    ],
 *    "body": "YXNk"
 * }
 * </pre>
 */
public interface JsonSerializationFixed {
}
