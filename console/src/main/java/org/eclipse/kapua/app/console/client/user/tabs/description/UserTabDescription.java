package org.eclipse.kapua.app.console.client.user.tabs.description;

import org.eclipse.kapua.app.console.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserTabDescription extends EntityDescriptionTabItem<GwtUser>{
    
    public UserTabDescription() {
    }
    
    private static final GwtUserServiceAsync GwtUserServiceAsync = GWT.create(GwtUserService.class);
    @Override
    protected RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy() {
       return new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

        @Override
        protected void load(Object loadConfig,
                AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
           GwtUserServiceAsync.getUserDescription(selectedEntity.getScopeId(), selectedEntity.getId(), callback);
            
        }
    };
    }

}
