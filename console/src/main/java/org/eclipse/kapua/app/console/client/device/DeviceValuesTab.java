package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceValuesTab extends TabItem{
    
    private GwtSession m_currentSession;
    private GwtDevice m_selectedDevice;
    private boolean m_dirty;
    private boolean m_initialized;
    private AssetTable table;
    private AssetDetailsTable assetDetailsTable;
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    
    

    public DeviceValuesTab(GwtSession currentSession){
        super(MSGS.valueTabItemTitle(), null);
        this.setBorders(false);
        this.setLayout(new FitLayout());
        m_currentSession = currentSession;
        m_dirty = true;
        m_initialized = false;
    }
    
    public void setDevice(GwtDevice selectedDevice){
        m_dirty = true;
        m_selectedDevice = selectedDevice;
    }
    
    public void refresh(){
        if (m_dirty && m_initialized) {
            m_dirty = false;
            if (m_selectedDevice == null) {
                
            } else {

            }
        }
    }
    
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        this.setWidth("100%");
        LayoutContainer tables = new LayoutContainer(new BorderLayout());
        add(tables);
        BorderLayoutData assetLayout = new BorderLayoutData(LayoutRegion.WEST, 0.3f);
        table = new AssetTable(m_currentSession);
        table.showToolbar(false);
        assetLayout.setMargins(new Margins(0, 5, 0, 0));
        assetLayout.setSplit(true);
        tables.add(table, assetLayout);
        LayoutContainer rightContainer = new LayoutContainer(new BorderLayout());
        BorderLayoutData rightBorder = new BorderLayoutData(LayoutRegion.CENTER, 0.7f);
        tables.add(rightContainer, rightBorder);
        BorderLayoutData detailsBorder = new BorderLayoutData(LayoutRegion.CENTER, 0.6f);
        assetDetailsTable = new AssetDetailsTable(m_currentSession);
        detailsBorder.setMargins(new Margins(0, 5, 0, 0));
        detailsBorder.setSplit(true);
        rightContainer.add(assetDetailsTable, detailsBorder);
        
    }
}
