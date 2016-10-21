package org.eclipse.kapua.app.console.client.ui.button;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

public class Button extends com.extjs.gxt.ui.client.widget.button.Button {

    private String originalText;
    private KapuaIcon icon;

    public Button(String text, KapuaIcon icon, SelectionListener<ButtonEvent> listener) {
        super();
        setText(text);
        setIcon(icon);
        addSelectionListener(listener);
    }

    @Override
    public String getText() {
        return originalText;
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
