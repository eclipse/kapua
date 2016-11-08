package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.widget.ComboEnumCellEditor;
import org.eclipse.kapua.app.console.client.ui.widget.EntityGridField;
import org.eclipse.kapua.app.console.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;

public class RolePermissionGridField extends EntityGridField<GwtRolePermission> {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    protected RolePermissionGridField(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        // ColumnConfig columnConfig = new ColumnConfig("id", "id", 100);
        // columnConfigs.add(columnConfig);
        //
        // columnConfig = new ColumnConfig("createdOnFormatted", "Created On", 100);
        // columnConfigs.add(columnConfig);

        SimpleComboBox<GwtDomain> domainCombo = new SimpleComboBox<GwtDomain>();
        domainCombo.add(Arrays.asList(GwtDomain.values()));

        ColumnConfig columnConfig = new ColumnConfig("domain", MSGS.dialogAddFieldGridRolePermissionDomain(), 100);
        columnConfig.setEditor(new ComboEnumCellEditor<GwtDomain>(domainCombo));
        columnConfigs.add(columnConfig);

        SimpleComboBox<GwtAction> actionCombo = new SimpleComboBox<GwtAction>();
        actionCombo.add(Arrays.asList(GwtAction.values()));

        columnConfig = new ColumnConfig("action", MSGS.dialogAddFieldGridRolePermissionAction(), 100);
        columnConfig.setEditor(new ComboEnumCellEditor<GwtAction>(actionCombo));
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
