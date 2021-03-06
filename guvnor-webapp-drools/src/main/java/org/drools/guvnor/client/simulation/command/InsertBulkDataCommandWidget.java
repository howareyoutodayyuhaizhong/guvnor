/*
 * Copyright 2012 JBoss Inc
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

package org.drools.guvnor.client.simulation.command;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.guvnor.shared.simulation.command.AssertRuleFiredCommandModel;
import org.drools.guvnor.shared.simulation.command.InsertBulkDataCommandModel;

public class InsertBulkDataCommandWidget extends AbstractCommandWidget<InsertBulkDataCommandModel> {

    protected interface InsertBulkDataCommandWidgetBinder extends UiBinder<Widget, InsertBulkDataCommandWidget> {}
    private static InsertBulkDataCommandWidgetBinder uiBinder = GWT.create(InsertBulkDataCommandWidgetBinder.class);

    @UiField
    protected FlowPanel flowPanel;

    public InsertBulkDataCommandWidget(InsertBulkDataCommandModel command) {
        super(command);
        initWidget(uiBinder.createAndBindUi(this));
    }

}
