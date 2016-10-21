package org.eclipse.kapua.app.console.client.ui.tab;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

public class TabItem extends com.extjs.gxt.ui.client.widget.TabItem {

    private String originalText;
    private KapuaIcon icon;

    public TabItem(String text, KapuaIcon icon) {
        super();
        setText(text);
        setIcon(icon);
    }

    @Override
    public void setText(String text) {
        super.setText((icon != null ? icon.getInlineHTML() + "&nbsp;&nbsp;" : "") + text);
        this.originalText = text;
    }

    public void setIcon(KapuaIcon icon) {
        super.setText(icon.getInlineHTML() + "&nbsp;&nbsp;" + originalText);
        this.icon = icon;
    }
}
