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

package org.drools.guvnor.server.simulation;

import com.google.gwt.user.client.rpc.SerializationException;
import org.drools.guvnor.client.rpc.Asset;
import org.drools.guvnor.server.contenthandler.PlainTextContentHandler;
import org.drools.guvnor.shared.simulation.SimulationModel;
import org.drools.ide.common.client.modeldriven.testing.Scenario;
import org.drools.ide.common.server.util.ScenarioXMLPersistence;
import org.drools.repository.AssetItem;

public class SimulationTestContentHandler extends PlainTextContentHandler {

    @Override
    public void retrieveAssetContent(Asset asset, AssetItem assetItem) {
        SimulationModel simulationModel = SimulationTestXMLPersistence.getInstance().unmarshal(assetItem.getContent());
        asset.setContent( simulationModel );
    }

    @Override
    public void storeAssetContent(Asset asset, AssetItem assetItem) {
        SimulationModel simulationModel = (SimulationModel) asset.getContent();
        assetItem.updateContent(SimulationTestXMLPersistence.getInstance().marshal(simulationModel));
    }

}
