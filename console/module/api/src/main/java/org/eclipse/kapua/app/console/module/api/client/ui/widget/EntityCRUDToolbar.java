/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class EntityCRUDToolbar<M extends GwtEntityModel> extends ToolBar {

    private final static ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    protected GwtSession currentSession;

    protected EntityGrid<M> entityGrid;

    protected GridSelectionModel<M> gridSelectionModel;

    protected ToolBar thisToolbar = this;

    protected AddButton addEntityButton;
    private boolean addEntityButtonShow = true;

    protected EditButton editEntityButton;
    private boolean editEntityButtonShow = true;

    protected DeleteButton deleteEntityButton;
    private boolean deleteEntityButtonShow = true;

    protected RefreshButton refreshEntityButton;
    private boolean refreshEntityButtonShow = true;

    protected ToggleButton filterButton;
    private boolean filterButtonShow = true;

    protected EntityFilterPanel<M> filterPanel;

    public EntityCRUDToolbar(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        if (addEntityButtonShow) {
            addEntityButton = new AddButton(getAddButtonSelectionListener());
            add(addEntityButton);
            add(new SeparatorToolItem());
        }

        if (editEntityButtonShow) {
            editEntityButton = new EditButton(getEditButtonSelectionListener());
            add(editEntityButton);
            add(new SeparatorToolItem());
        }

        if (deleteEntityButtonShow) {
            deleteEntityButton = new DeleteButton(getDeleteButtonSelectionListener());
            add(deleteEntityButton);
            add(new SeparatorToolItem());
        }

        if (refreshEntityButtonShow) {
            refreshEntityButton = new RefreshButton(getRefreshButtonSelectionListener());
            add(refreshEntityButton);
        }

        if (filterButtonShow && filterPanel != null) {
            //FIXME when providing additional buttons and also the filter button it will probably display weird
            add(new FillToolItem());
            filterButton = new ToggleButton(MSGS.deviceTableToolbarCloseFilter(), new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (filterButton.isPressed()) {
                        filterPanel.show();
                        filterButton.setText(MSGS.deviceTableToolbarCloseFilter());
                    } else {
                        filterPanel.hide();
                        filterButton.setText(MSGS.deviceTableToolbarOpenFilter());
                    }

                }
            });
            filterButton.toggle(true);
            add(filterButton);
        }
    }

    //
    // Add button methods
    //
    protected SelectionListener<ButtonEvent> getAddButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getAddDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getAddDialog() {
        return null;
    }

    public void setAddButtonVisible(boolean show) {
        this.addEntityButtonShow = show;
    }

    //
    // Edit button methods
    //
    protected SelectionListener<ButtonEvent> getEditButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getEditDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getEditDialog() {
        return null;
    }

    public void setEditButtonVisible(boolean show) {
        this.editEntityButtonShow = show;
    }

    public void setFilterButtonVisible(boolean show) {
        this.filterButtonShow = show;
    }

    //
    // Delete button methods
    //
    protected SelectionListener<ButtonEvent> getDeleteButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getDeleteDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getDeleteDialog() {
        return null;
    }

    public void setDeleteButtonVisible(boolean show) {
        this.deleteEntityButtonShow = show;
    }

    //
    // Refresh button methods
    //
    protected SelectionListener<ButtonEvent> getRefreshButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                thisToolbar.disable();
                entityGrid.refresh();
                thisToolbar.enable();
            }
        };
    }

    public void setRefreshButtonVisible(boolean show) {
        this.refreshEntityButtonShow = show;
    }

    //
    // Other methods
    private Listener<? extends BaseEvent> getHideDialogListener() {
        return new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {

                //
                // Show exit popup
                ActionDialog dialog = be.getComponent();
                if (dialog.getExitStatus() != null) {
                    if (dialog.getExitStatus()) {
                        ConsoleInfo.display(MSGS.popupInfo(), dialog.getExitMessage());
                    } else {
                        ConsoleInfo.display(MSGS.popupError(), dialog.getExitMessage());
                    }
                }

                entityGrid.refresh();
                thisToolbar.enable();
            }
        };
    }

    public void setEntityGrid(EntityGrid<M> entityGrid) {
        this.entityGrid = entityGrid;
        this.gridSelectionModel = entityGrid.getSelectionModel();
    }

    protected void disableButtons() {
        if (addEntityButton != null) {
            addEntityButton.disable();
        }

        if (editEntityButton != null) {
            editEntityButton.disable();
        }

        if (deleteEntityButton != null) {
            deleteEntityButton.disable();
        }

        if (refreshEntityButton != null) {
            refreshEntityButton.disable();
        }
    }

    protected void enableButtons() {
        if (addEntityButton != null) {
            addEntityButton.enable();
        }

        if (editEntityButton != null) {
            editEntityButton.enable();
        }

        if (deleteEntityButton != null) {
            deleteEntityButton.enable();
        }

        if (refreshEntityButton != null) {
            refreshEntityButton.enable();
        }

        if (filterButton != null) {
            filterButton.enable();
        }
    }

    public AddButton getAddEntityButton() {
        return addEntityButton;
    }

    public EditButton getEditEntityButton() {
        return editEntityButton;
    }

    public DeleteButton getDeleteEntityButton() {
        return deleteEntityButton;
    }

    public RefreshButton getRefreshEntityButton() {
        return refreshEntityButton;
    }

    public ToggleButton getFilterButton() {
        return filterButton;
    }

    public void setFilterPanel(EntityFilterPanel<M> filterPanel) {
        this.filterPanel = filterPanel;
    }
}
