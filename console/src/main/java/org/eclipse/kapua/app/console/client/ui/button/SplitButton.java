package org.eclipse.kapua.app.console.client.ui.button;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

public class SplitButton extends com.extjs.gxt.ui.client.widget.button.SplitButton {

    private String originalText;
    private KapuaIcon icon;

    public SplitButton(String text, KapuaIcon icon) {
        super();
        setText(text);
        setIcon(icon);
    }

    @Override
    public String getText() {
        return originalText;
    }

    @Override
    public void setText(String text) {
        super.setText((icon != null ? icon.getInlineHTML() + " " : "") + text);
        this.originalText = text;
    }

    public void setIcon(KapuaIcon icon) {
        super.setText(icon.getInlineHTML() + " " + originalText);
        this.icon = icon;
    }

}
