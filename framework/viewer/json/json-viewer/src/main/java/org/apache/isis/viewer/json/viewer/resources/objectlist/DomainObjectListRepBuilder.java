/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.viewer.json.viewer.resources.objectlist;

import java.util.List;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.viewer.json.applib.JsonRepresentation;
import org.apache.isis.viewer.json.viewer.ResourceContext;
import org.apache.isis.viewer.json.viewer.representations.RepresentationBuilder;
import org.apache.isis.viewer.json.viewer.representations.WellKnownType;
import org.apache.isis.viewer.json.viewer.resources.objects.DomainObjectRepBuilder;

public class DomainObjectListRepBuilder extends RepresentationBuilder<DomainObjectListRepBuilder> {

    protected DomainObjectListRepBuilder(ResourceContext resourceContext, String typeName, List<ObjectAdapter> objectAdapters) {
        this(resourceContext);
    }

    protected DomainObjectListRepBuilder(ResourceContext resourceContext) {
        super(resourceContext);
    }

    public DomainObjectListRepBuilder withRepresentationTypeListOf(String typeName) {
        return withRepresentationType("list:" + typeName);
    }

    public DomainObjectListRepBuilder withRepresentationTypeListOf(ObjectSpecification objectSpec) {
        return withRepresentationTypeListOf(WellKnownType.canonical(objectSpec.getFullIdentifier()));
    }

    public DomainObjectListRepBuilder withAdapters(List<ObjectAdapter> objectAdapters) {
        JsonRepresentation list = JsonRepresentation.newArray();
        for(ObjectAdapter adapter: objectAdapters) {
            JsonRepresentation linkToObject = buildLinkTo(adapter);
            list.add(linkToObject);
        }
        representation.put("value", list);
        return this;
    }


    public JsonRepresentation build() {
        withLinks();
        withMetadata();

        return representation;
    }


    protected JsonRepresentation buildLinkTo(ObjectAdapter adapter) {
        return DomainObjectRepBuilder.newLinkToBuilder(resourceContext, adapter, getOidStringifier()).build();
    }


}