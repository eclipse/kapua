package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class AssetTabItem extends TabItem{
    private GwtSession m_currentSession;
    private TabPanel m_tabsPanel;
    private DeviceValuesTab valuesTab;
    private DeviceConfigurationTab configurationsTab;
    private TabItem m_tabValue;
    private TabItem m_tabConfiguration;
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    
    public AssetTabItem(GwtSession currentSession){
        super(MSGS.assetTabItemTitle(), null);
        m_currentSession = currentSession;
        valuesTab = new DeviceValuesTab(m_currentSession);
        configurationsTab = new DeviceConfigurationTab(m_currentSession);
    }
    
    @Override
    protected void onRender(Element parent, int index) {
        
        super.onRender(parent, index);
        setLayout(new FitLayout());
        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setBodyBorder(false);
        m_tabValue = new TabItem(MSGS.valueTabItem(), new KapuaIcon(IconSet.INFO));
        m_tabValue.setBorders(true);
        m_tabValue.setLayout(new FitLayout());
        m_tabValue.add(valuesTab);
        m_tabsPanel.add(m_tabValue);
        m_tabConfiguration = new TabItem(MSGS.configurationTabItem(), new KapuaIcon(IconSet.CODE));
        m_tabConfiguration.setBorders(true);
        m_tabConfiguration.setLayout(new FitLayout());
        m_tabConfiguration.add(configurationsTab);
        m_tabsPanel.add(m_tabConfiguration);
        m_tabsPanel.setTabPosition(TabPosition.BOTTOM);
        add(m_tabsPanel);
    }
}
