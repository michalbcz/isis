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


package org.apache.isis.runtime.persistence.objectstore.algorithm.dflt;

import org.apache.log4j.Logger;
import org.apache.isis.commons.lang.ToString;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.adapter.ResolveState;
import org.apache.isis.metamodel.commons.exceptions.UnknownTypeException;
import org.apache.isis.metamodel.facets.collections.modify.CollectionFacet;
import org.apache.isis.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.apache.isis.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.metamodel.util.CallbackUtils;
import org.apache.isis.metamodel.util.CollectionFacetUtils;
import org.apache.isis.runtime.persistence.objectstore.algorithm.PersistAlgorithmAbstract;
import org.apache.isis.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;
import org.apache.isis.runtime.transaction.ObjectPersistenceException;


public class DefaultPersistAlgorithm extends PersistAlgorithmAbstract {
    private static final Logger LOG = Logger.getLogger(DefaultPersistAlgorithm.class);

    public String name() {
        return "Simple Bottom Up Persistence Walker";
    }

    public void makePersistent(final ObjectAdapter object, final ToPersistObjectSet toPersistObjectSet) {
        if (object.getSpecification().isCollection()) {
            LOG.info("persist " + object);
            if (object.getResolveState() == ResolveState.GHOST) {
                object.changeState(ResolveState.RESOLVING);
                object.changeState(ResolveState.RESOLVED);
            } else if (object.getResolveState() == ResolveState.TRANSIENT) {
                object.changeState(ResolveState.RESOLVED);
            }
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(object);
            for (ObjectAdapter element: facet.iterable(object)) {
                persist(element, toPersistObjectSet);
            }
        } else {
            assertObjectNotPersistentAndPersistable(object);
            persist(object, toPersistObjectSet);
        }
    }

    protected void persist(final ObjectAdapter object, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistableOrServiceOrStandalone(object)) {
            return;
        }
        
        final ObjectAssociation[] fields = object.getSpecification().getAssociations();
        if (!object.getSpecification().isEncodeable() && fields.length > 0) {
            LOG.info("make persistent " + object);
            CallbackUtils.callCallback(object, PersistingCallbackFacet.class);
            toPersistObjectSet.remapAsPersistent(object);

            for (int i = 0; i < fields.length; i++) {
                final ObjectAssociation field = fields[i];
                if (field.isNotPersisted()) {
                    continue;
                }
                if (field.isOneToManyAssociation()) {
                    final ObjectAdapter collection = field.get(object);
                    if (collection == null) {
                        throw new ObjectPersistenceException(
                                "Collection " + field.getName() + 
                                " does not exist in " + object.getSpecification().getFullName());
                    }
                    makePersistent(collection, toPersistObjectSet);
                } else {
                    final ObjectAdapter fieldValue = field.get(object);
                    if (fieldValue == null) {
                        continue;
                    }
                    persist(fieldValue, toPersistObjectSet);
                }
            }
            toPersistObjectSet.addPersistedObject(object);
            CallbackUtils.callCallback(object, PersistedCallbackFacet.class);
        }
        
    }

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        return toString.toString();
    }

}