package org.drools.guvnor.server.builder.pagerow;

/*
 * Copyright 2011 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.guvnor.client.rpc.QueryPageRequest;
import org.drools.guvnor.client.rpc.QueryPageRow;
import org.drools.guvnor.server.CategoryFilter;
import org.drools.guvnor.server.util.QueryPageRowCreator;
import org.drools.repository.AssetItem;
import org.drools.repository.RepositoryFilter;

public class QueryFullTextPageRowBuilder
    implements
    PageRowBuilder<QueryPageRequest, Iterator<AssetItem>> {

    private QueryPageRequest    pageRequest;
    private Iterator<AssetItem> iterator;

    public List<QueryPageRow> build() {
        validate();
        int skipped = 0;
        Integer pageSize = pageRequest.getPageSize();
        int startRowIndex = pageRequest.getStartRowIndex();
        RepositoryFilter categoryFilter = new CategoryFilter();

        List<QueryPageRow> rowList = new ArrayList<QueryPageRow>();

        while (iterator.hasNext() && (pageSize == null || rowList.size() < pageSize)) {
            AssetItem assetItem = iterator.next();

            // Cannot use AssetItemIterator.skip() as it skips non-filtered
            // assets whereas startRowIndex is the index of the
            // first displayed asset (i.e. filtered)
            if (skipped >= startRowIndex) {
                rowList.add(QueryPageRowCreator.makeQueryPageRow(assetItem));
            }
            skipped++;
        }
        return rowList;
    }

    public void validate() {
        if ( pageRequest == null ) {
            throw new IllegalArgumentException( "PageRequest cannot be null" );
        }

        if ( iterator == null ) {
            throw new IllegalArgumentException( "Content cannot be null" );
        }

    }

    public QueryFullTextPageRowBuilder withPageRequest(QueryPageRequest pageRequest) {
        this.pageRequest = pageRequest;
        return this;
    }

    public QueryFullTextPageRowBuilder withContent(Iterator<AssetItem> iterator) {
        this.iterator = iterator;
        return this;
    }
}
