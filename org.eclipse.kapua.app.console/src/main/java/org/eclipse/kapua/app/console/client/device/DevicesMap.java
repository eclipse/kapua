/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.RenderIntent;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.Attribution;
import org.gwtopenmaps.openlayers.client.control.DragPan;
import org.gwtopenmaps.openlayers.client.control.Navigation;
import org.gwtopenmaps.openlayers.client.control.PanZoom;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.SelectFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.FeatureHighlightedListener;
import org.gwtopenmaps.openlayers.client.event.FeatureUnhighlightedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.OSMOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.popup.AnchoredBubble;
import org.gwtopenmaps.openlayers.client.util.Attributes;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DevicesMap extends LayoutContainer {

    private final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private static final Projection DEFAULT_PROJECTION = new Projection("EPSG:4326");

    private DevicesView m_devicesView;
    private GwtSession m_currentSession;

    private Map m_map;
    private MapWidget m_mapWidget;
    private Vector m_markerLayer;
    private AnchoredBubble m_popup;

    public DevicesMap(DevicesView devicesView,
            GwtSession currentSession) {
        m_devicesView = devicesView;
        m_currentSession = currentSession;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // create some MapOptions
        MapOptions defaultMapOptions = new MapOptions();
        defaultMapOptions.setNumZoomLevels(16);
        defaultMapOptions.removeDefaultControls();

        // Create a MapWidget
        m_mapWidget = new MapWidget("500px", "500px", defaultMapOptions);

        // create the mapquest base layer
        OSMOptions osmOptions = new OSMOptions();
        osmOptions.setAttribution(
                "<p>Tiles Courtesy of <a href=\"http://www.mapquest.com/\" target=\"_blank\">MapQuest</a> <img src=\"http://developer.mapquest.com/content/osm/mq_logo.png\"></p> - &copy; <a href=\"http://www.openstreetmap.org/copyright\" target=\"_blank\">OpenStreetMap</a> contributors");
        OSM mapQuest = new OSM("Map", "http://otile1.mqcdn.com/tiles/1.0.0/map/${z}/${x}/${y}.png", osmOptions);
        mapQuest.setIsBaseLayer(true);

        // Create a marker layer to the current location marker
        m_markerLayer = new Vector(MSGS.devices());

        // add selection
        SelectFeature selectFeature = new SelectFeature(m_markerLayer);

        // And add them to the map
        m_map = m_mapWidget.getMap();
        m_map.addLayer(mapQuest);
        m_map.addLayer(m_markerLayer);
        m_map.addControl(selectFeature);

        // Lets add some default controls to the map
        m_map.addControl(new Attribution()); // Display the scaleline
        m_map.addControl(new Navigation()); // Display the scaleline
        m_map.addControl(new DragPan()); // Display the scaleline
        m_map.addControl(new PanZoom()); // Display the scaleline
        m_map.addControl(new ScaleLine()); // Display the scaleline

        // Center and zoom to a location
        // transform lonlat to OSM coordinate system
        // Open a map centered in Amaro, Italy. Required
        LonLat lonLat = new LonLat(13.097, 46.375);
        lonLat.transform(DEFAULT_PROJECTION.getProjectionCode(), m_map.getProjection());
        m_map.setCenter(lonLat, 12);

        add(m_mapWidget);

        // force the map to fall behind popups
        m_mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0);
    }

    public void refresh(GwtDeviceQueryPredicates predicates) {
        // clean-up the Marker list
        m_markerLayer.destroyFeatures();
        if (m_popup != null) {
            m_map.removePopup(m_popup);
        }

        // reload the devices for this account
        final DevicesMap theDeviceMap = this;
        theDeviceMap.mask(MSGS.loading());
        BasePagingLoadConfig loadConfig = new BasePagingLoadConfig(0, 500);
        gwtDeviceService.findDevices(loadConfig,
                m_currentSession.getSelectedAccount().getId(),
                predicates,
                new AsyncCallback<PagingLoadResult<GwtDevice>>() {

                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                        theDeviceMap.unmask();
                    }

                    public void onSuccess(PagingLoadResult<GwtDevice> loadResult) {
                        placeMarkers(loadResult.getData());
                        theDeviceMap.unmask();
                    }
                });
    }

    private void placeMarkers(List<GwtDevice> devices) {
        LonLat lonLat = null;
        final List<GwtDevice> theDevices = devices;
        for (int i = 0; i < devices.size(); i++) {

            final GwtDevice theDevice = devices.get(i);
            if (theDevice.getGpsLatitude() != null && theDevice.getGpsLongitude() != null) {

                StringBuilder sbDeviceTitle = new StringBuilder();
                if (theDevice.getDisplayName() != null) {
                    sbDeviceTitle.append(theDevice.getDisplayName())
                            .append(" (")
                            .append(theDevice.getClientId())
                            .append(")");
                } else {
                    sbDeviceTitle.append(theDevice.getClientId());
                }

                lonLat = new LonLat(theDevice.getGpsLongitude(), theDevice.getGpsLatitude());
                lonLat.transform(DEFAULT_PROJECTION.getProjectionCode(), m_map.getProjection());

                // lets create a vector point on the location
                Style pointStyle = new Style();
                pointStyle.setFillOpacity(0.9);
                pointStyle.setExternalGraphic("eclipse/org/eclipse/kapua/app/console/icon/device_map.png");
                pointStyle.setGraphicSize(37, 34);
                pointStyle.setGraphicOffset(-10, -34);
                pointStyle.setGraphicName(theDevice.getDisplayName());

                Point point = new Point(theDevice.getGpsLongitude(), theDevice.getGpsLatitude());
                point.transform(DEFAULT_PROJECTION, new Projection(m_map.getProjection())); // transform point to OSM coordinate system

                VectorFeature pointFeature = new VectorFeature(point, pointStyle);
                Attributes attributes = new Attributes();
                attributes.setAttribute("deviceIdx", i);
                pointFeature.setAttributes(attributes);

                m_markerLayer.addFeature(pointFeature);
            }
        }

        // Click SelectFeature and its Options
        SelectFeatureOptions clickFeatureOptions = new SelectFeatureOptions();
        clickFeatureOptions.onSelect(new SelectFeatureListener() {

            @Override
            public void onFeatureSelected(VectorFeature theVectorFeature) {
                int deviceIdx = theVectorFeature.getAttributes().getAttributeAsInt("deviceIdx");
                GwtDevice device = theDevices.get(deviceIdx);
                if (device != null) {
                    m_devicesView.setDevice(device);
                }
            }
        });
        SelectFeature clickFeature = new SelectFeature(m_markerLayer, clickFeatureOptions);
        clickFeature.setClickOut(true);
        clickFeature.setMultiple(false);

        // Hover SelectFeature and its Options
        SelectFeatureOptions hoverFeatureOptions = new SelectFeatureOptions();
        hoverFeatureOptions.setHover();
        hoverFeatureOptions.setHighlightOnly(true);
        hoverFeatureOptions.setRenderIntent(RenderIntent.DEFAULT);

        final SelectFeature hoverFeature = new SelectFeature(m_markerLayer, hoverFeatureOptions);
        hoverFeature.setClickOut(true);
        hoverFeature.setMultiple(false);
        hoverFeature.addFeatureHighlightedListener(new FeatureHighlightedListener() {

            @Override
            public void onFeatureHighlighted(VectorFeature theVectorFeature) {
                int deviceIdx = theVectorFeature.getAttributes().getAttributeAsInt("deviceIdx");
                GwtDevice device = theDevices.get(deviceIdx);
                if (device != null) {
                    if (m_popup != null) {
                        m_map.removePopup(m_popup);
                    }
                    LonLat lonLat = theVectorFeature.getCenterLonLat();
                    m_popup = new AnchoredBubble("marker-info",
                            lonLat,
                            new Size(185, 20),
                            "<p>" + device.getDisplayName() + " (" + device.getClientId() + ")</p>",
                            null,
                            false);
                    m_map.addPopup(m_popup);
                }
            }
        });
        hoverFeature.addFeatureUnhighlightedListener(new FeatureUnhighlightedListener() {

            @Override
            public void onFeatureUnhighlighted(VectorFeature eventObject) {
                if (m_popup != null) {
                    m_map.removePopup(m_popup);
                    m_popup = null;
                }
                hoverFeature.unselectAll(null);
            }
        });

        m_map.addControl(clickFeature);
        m_map.addControl(hoverFeature);

        hoverFeature.activate();
        clickFeature.activate();

        // auto-adjust the zoom to show all points
        if (m_markerLayer.getDataExtent() != null) {
            m_map.zoomToExtent(m_markerLayer.getDataExtent());
        } else {
            m_map.zoomTo(1);
        }
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    public void onUnload() {
        // clean-up the Marker list
        // for (Marker marker : m_markers) {
        // Event.clearInstanceListeners(marker);
        // }
        super.onUnload();
    }

    public void onResize(int width, int height) {
        // m_mapWidget.setSize(String.valueOf(width), String.valueOf(height));
        super.onResize(width, height);
    }
}
