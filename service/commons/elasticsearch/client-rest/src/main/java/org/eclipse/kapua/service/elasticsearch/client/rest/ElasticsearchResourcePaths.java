/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.rest;

import org.eclipse.kapua.service.elasticsearch.client.model.InsertRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;

import javax.validation.constraints.NotNull;

/**
 * {@link RestElasticsearchClient} resource paths.
 *
 * @since 1.3.0
 */
public class ElasticsearchResourcePaths {

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    private ElasticsearchResourcePaths() {
    }

    /**
     * @since 1.0.0
     */
    public static String getBulkPath() {
        return "/_bulk";
    }

    /**
     * @since 1.0.0
     */
    public static String deleteByQuery(@NotNull TypeDescriptor typeDescriptor) {
        return String.format("/%s/_delete_by_query", typeDescriptor.getIndex());
    }

    /**
     * @since 1.0.0
     */
    public static String findIndex(String index) {
        return String.format("/_cat/indices?h=index&index=%s", index);
    }

    /**
     * @since 1.0.0
     */
    public static String id(@NotNull TypeDescriptor typeDescriptor, @NotNull String id) {
        return String.format("/%s/_doc/%s", typeDescriptor.getIndex(), id);
    }

    /**
     * @since 1.0.0
     */
    public static String index(String index) {
        return String.format("/%s", index);
    }

    /**
     * @since 1.0.0
     */
    public static String insertType(@NotNull InsertRequest request) {
        if (request.getId() != null) {
            return String.format("%s/%s", type(request.getTypeDescriptor()), request.getId());
        } else {
            return type(request.getTypeDescriptor());
        }
    }

    /**
     * @since 1.0.0
     */
    public static String mapping(@NotNull TypeDescriptor typeDescriptor) {
        return String.format("/%s/_mapping", typeDescriptor.getIndex());
    }

    /**
     * @since 1.0.0
     */
    public static String search(@NotNull TypeDescriptor typeDescriptor) {
        return String.format("/%s/_search", typeDescriptor.getIndex());
    }

    /**
     * @since 1.0.0
     */
    public static String refreshAllIndexes() {
        return "/_all/_refresh";
    }

    /**
     * @since 1.0.0
     */
    public static String type(@NotNull TypeDescriptor typeDescriptor) {
        return String.format("/%s/_doc", typeDescriptor.getIndex());
    }

    /**
     * @since 1.0.0
     */
    public static String upsert(@NotNull TypeDescriptor typeDescriptor, @NotNull String id) {
        return String.format("/%s/_update/%s", typeDescriptor.getIndex(), id);
    }

}
