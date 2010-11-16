/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.core.progmodel.facets.object.dirty;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.isis.core.metamodel.facets.Facet;
import org.apache.isis.core.metamodel.facets.FacetHolder;
import org.apache.isis.core.metamodel.facets.FacetUtil;
import org.apache.isis.core.metamodel.facets.MethodRemover;
import org.apache.isis.core.metamodel.spec.feature.ObjectFeatureType;
import org.apache.isis.core.progmodel.java5.MethodPrefixBasedFacetFactoryAbstract;



public class DirtyMethodsFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String MARK_DIRTY_PREFIX = "markDirty";
    private static final String CLEAR_DIRTY_PREFIX = "clearDirty";
    private static final String IS_DIRTY_PREFIX = "isDirty";

    private static final String[] PREFIXES = { MARK_DIRTY_PREFIX, CLEAR_DIRTY_PREFIX, IS_DIRTY_PREFIX, };

    public DirtyMethodsFacetFactory() {
        super(PREFIXES, ObjectFeatureType.OBJECTS_ONLY);

    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();

        Method method = findMethod(cls, OBJECT, IS_DIRTY_PREFIX, boolean.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new IsDirtyObjectFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, CLEAR_DIRTY_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new ClearDirtyObjectFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, MARK_DIRTY_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new MarkDirtyObjectFacetViaMethod(method, facetHolder));
        }

        return FacetUtil.addFacets(facets);
    }

}