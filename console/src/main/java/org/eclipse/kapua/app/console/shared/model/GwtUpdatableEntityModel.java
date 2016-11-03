package org.eclipse.kapua.app.console.shared.model;

import java.util.Date;

import org.eclipse.kapua.app.console.client.util.DateUtils;

public abstract class GwtUpdatableEntityModel extends GwtEntityModel {

    private static final long serialVersionUID = -8268981197541435575L;

    public GwtUpdatableEntityModel() {
        super();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("modifiedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getModifiedOn()));
        } else {
            return super.get(property);
        }
    }

    public Date getModifiedOn() {
        return get("modifiedOn");
    }

    public String getModifiedOnFormatted() {
        return get("modifiedOnFormatted");
    }

    public void setModifiedOn(Date modifiedOn) {
        set("modifiedOn", modifiedOn);
    }

    public String getModifiedBy() {
        return get("modifiedBy");
    }

    public void setModifiedBy(String modifiedBy) {
        set("modifiedBy", modifiedBy);
    }

    public int getOptlock() {
        return get("optlock");
    }

    public void setOptlock(int optlock) {
        set("optlock", optlock);
    }
}