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


package org.apache.isis.metamodel.facets.disable;

import org.apache.isis.applib.events.UsabilityEvent;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.facets.Facet;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.SingleWhenValueFacetAbstract;
import org.apache.isis.metamodel.facets.When;
import org.apache.isis.metamodel.interactions.UsabilityContext;


public abstract class DisabledFacetAbstract extends SingleWhenValueFacetAbstract implements DisabledFacet {

    public static Class<? extends Facet> type() {
        return DisabledFacet.class;
    }

    public DisabledFacetAbstract(final When value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final ObjectAdapter target = ic.getTarget();
        String disabledReason = disabledReason(target);
        if (disabledReason != null) {
        	return disabledReason;
        }
        if (getUnderlyingFacet() != null) {
        	DisabledFacet underlyingFacet = (DisabledFacet) getUnderlyingFacet();
        	return underlyingFacet.disabledReason(target);
        }
        return null;
    }

}