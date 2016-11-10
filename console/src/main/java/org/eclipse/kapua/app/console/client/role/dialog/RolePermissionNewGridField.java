package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.widget.EntityGridField;
import org.eclipse.kapua.app.console.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.client.ui.widget.EnumComboBox;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;

public class RolePermissionNewGridField extends EntityGridField<GwtRolePermission> {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    protected RolePermissionNewGridField(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        EnumComboBox<GwtDomain> domainCombo = new EnumComboBox<GwtDomain>();
        domainCombo.add(GwtDomain.values());

        ColumnConfig columnConfig = new ColumnConfig("domain", MSGS.dialogAddFieldGridRolePermissionDomain(), 100);
        columnConfig.setEditor(new GwtDomainComboCellEditor(domainCombo));
        columnConfigs.add(columnConfig);

        EnumComboBox<GwtAction> actionCombo = new EnumComboBox<GwtAction>();
        actionCombo.add(GwtAction.values());

        columnConfig = new ColumnConfig("action", MSGS.dialogAddFieldGridRolePermissionAction(), 100);
        columnConfig.setEditor(new GwtActionComboCellEditor(actionCombo));
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("targetScopeId", MSGS.dialogAddFieldGridRolePermissionTargetScopeId(), 100);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected EntityGridFieldToolbar<GwtRolePermission> getToolbar() {
        return new RolePermissionGridFieldToolbar(currentSession);
    }
}
