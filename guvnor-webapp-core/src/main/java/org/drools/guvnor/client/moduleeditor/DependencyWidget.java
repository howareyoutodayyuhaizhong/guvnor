/*
 * Copyright 2050 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.client.moduleeditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import org.drools.guvnor.client.asseteditor.MultiViewRow;
import org.drools.guvnor.client.common.FormStyleLayout;
import org.drools.guvnor.client.common.FormStylePopup;
import org.drools.guvnor.client.common.GenericCallback;
import org.drools.guvnor.client.common.InfoPopup;
import org.drools.guvnor.client.explorer.ClientFactory;
import org.drools.guvnor.client.messages.ConstantsCore;
import org.drools.guvnor.client.resources.ImagesCore;
import org.drools.guvnor.client.rpc.Module;
import org.drools.guvnor.client.rpc.ModuleService;
import org.drools.guvnor.client.rpc.ModuleServiceAsync;
import org.drools.guvnor.client.rpc.Path;
import org.drools.guvnor.client.rpc.PathImpl;
import org.drools.guvnor.client.widgets.VersionChooser;
import org.drools.guvnor.client.widgets.query.OpenItemCommand;
import org.drools.guvnor.client.widgets.tables.DependenciesPagedTable;

/**
 * This is the widget for building dependencies.
 */
public class DependencyWidget extends Composite {

    private DependenciesPagedTable table;

    private Module conf;
    private boolean isHistoricalReadOnly = false;
    private final ClientFactory clientFactory;
    private final EventBus eventBus;

    public DependencyWidget(ClientFactory clientFactory,
                            EventBus eventBus,
                            final Module conf,
                            boolean isHistoricalReadOnly) {
        this.clientFactory = clientFactory;
        this.eventBus = eventBus;
        this.conf = conf;
        this.isHistoricalReadOnly = isHistoricalReadOnly;
        FormStyleLayout layout = new FormStyleLayout();

        VerticalPanel header = new VerticalPanel();
        Label caption = new Label("Dependencies");
        caption.getElement().getStyle().setFontWeight(FontWeight.BOLD);
        header.add(caption);
        header.add(dependencyTip());

        layout.addAttribute("",
                header);

        /*        layout.addHeader( images.statusLarge(),
                              header );*/

        VerticalPanel vp = new VerticalPanel();
        vp.setHeight("100%");
        vp.setWidth("100%");

        //pf.startSection();
        layout.addRow(vp);

        table = new DependenciesPagedTable(conf.getUuid(),
                new OpenItemCommand() {
                    public void open(String path) {
                        showEditor(path);
                    }

                    public void open(MultiViewRow[] rows) {
                        // Do nothing, unsupported
                    }
                });

        layout.addRow(table);

        initWidget(layout);
    }

    private Widget dependencyTip() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new HTML("<small><i>"
                + "This shows exact versions of assets that this package contains."
                + "</i></small>"));
        InfoPopup pop = new InfoPopup("Edit Dependency",
                "Edit dependency version to build a package against specific versions of assets");
        hp.add(pop);
        return hp;
    }

    public static String[] decodeDependencyPath(String dependencyPath) {
        if (dependencyPath.indexOf("?version=") >= 0) {
            return dependencyPath.split("\\?version=");
        } else {
            return new String[]{dependencyPath, "LATEST"};
        }
    }

    public static String encodeDependencyPath(String dependencyPath,
                                              String dependencyVersion) {
        return dependencyPath + "?version=" + dependencyVersion;
    }

    private void showEditor(final String dependencyPath) {
        Image image = new Image(ImagesCore.INSTANCE.management());
        image.setAltText(ConstantsCore.INSTANCE.Management());
        final FormStylePopup editor = new FormStylePopup(image,
                "Edit Dependency");
        /*		editor.addRow(new HTML("<i>" + "Choose the version you want to depend on"
        				+ "</i>"));
        */
        editor.addAttribute("Dependency Path: ",
                new Label(decodeDependencyPath(dependencyPath)[0]));
        final VersionChooser versionChoose = new VersionChooser(clientFactory,
                eventBus,
                decodeDependencyPath(dependencyPath)[1],
                conf.getUuid(),
                decodeDependencyPath(dependencyPath)[0],
                new Command() {
                    public void execute() {
                        table.refresh();
                    }
                });
        editor.addAttribute("Dependency Version: ",
                versionChoose);

        HorizontalPanel hp = new HorizontalPanel();
        Button useSelectedVersionButton = new Button("Use selected version");
        hp.add(useSelectedVersionButton);
        useSelectedVersionButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent w) {
                String selectedVersion = versionChoose.getSelectedVersionName();
                if (selectedVersion == null) {
                    return;
                }
                if (Window.confirm("Are you sure you want to use version: " + selectedVersion + " as dependency?")) {
                    ModuleServiceAsync moduleService = GWT.create(ModuleService.class);
                    Path path = new PathImpl();
                    path.setUUID(conf.getUuid());
                    moduleService.updateDependency(
                    		path,
                            encodeDependencyPath(DependencyWidget
                                    .decodeDependencyPath(dependencyPath)[0],
                                    selectedVersion),
                            new GenericCallback<Void>() {
                                public void onSuccess(Void v) {
                                    editor.hide();
                                    table.refresh();
                                }
                            });
                }
            }
        });
        useSelectedVersionButton.setEnabled(!isHistoricalReadOnly);

        Button cancel = new Button(ConstantsCore.INSTANCE.Cancel());
        hp.add(cancel);
        cancel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent w) {
                editor.hide();
            }
        });

        editor.addAttribute("",
                hp);
        editor.show();
    }
}
