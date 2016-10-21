package org.eclipse.kapua.app.console.client.ui.panel;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.widget.Layout;

public class ContentPanel extends com.extjs.gxt.ui.client.widget.ContentPanel {

    private String originalHeading;
    private KapuaIcon icon;

    public ContentPanel() {
        super();
    }

    public ContentPanel(Layout layout) {
        super(layout);
    }

    @Override
    public String getHeading() {
        return originalHeading;
    }

    @Override
    public void setHeading(String heading) {
        super.setHeading((icon != null ? icon.getInlineHTML() + "&nbsp;&nbsp;" : "") + heading);
        this.originalHeading = heading;
    }

    public void setIcon(KapuaIcon icon) {
        super.setHeading(icon.getInlineHTML() + "&nbsp;&nbsp;" + originalHeading);
        this.icon = icon;
    }

}
