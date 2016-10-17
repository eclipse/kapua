package org.eclipse.kapua.app.console.shared.model.device.management.packages;

public class GwtPackageDownloadOperation extends GwtPackageOperation {

    private static final long serialVersionUID = 3656991206495864748L;

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return (String) get("id");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getStatus() {
        return (String) get("status");
    }

    public void setSize(Integer size) {
        set("size", size);
    }

    public Integer getSize() {
        return (Integer) get("size");
    }

    public void setProgress(Integer progress) {
        set("progress", progress);
    }

    public Integer getProgress() {
        return (Integer) get("progress");
    }
}
