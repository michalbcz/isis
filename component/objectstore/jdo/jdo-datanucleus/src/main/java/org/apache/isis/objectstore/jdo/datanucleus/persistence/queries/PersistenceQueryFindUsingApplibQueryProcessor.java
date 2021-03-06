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
package org.apache.isis.objectstore.jdo.datanucleus.persistence.queries;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.services.container.query.QueryCardinality;
import org.apache.isis.core.metamodel.spec.ObjectAdapterUtils;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.core.runtime.persistence.query.PersistenceQueryFindUsingApplibQueryDefault;
import org.apache.isis.objectstore.jdo.datanucleus.DataNucleusObjectStore;
import org.apache.isis.objectstore.jdo.datanucleus.metamodel.JdoPropertyUtils;
import org.apache.isis.objectstore.jdo.datanucleus.persistence.FrameworkSynchronizer;

public class PersistenceQueryFindUsingApplibQueryProcessor extends PersistenceQueryProcessorAbstract<PersistenceQueryFindUsingApplibQueryDefault> {
    
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceQueryFindUsingApplibQueryProcessor.class);

    public PersistenceQueryFindUsingApplibQueryProcessor(final PersistenceManager persistenceManager, final FrameworkSynchronizer frameworkSynchronizer) {
        super(persistenceManager, frameworkSynchronizer);
    }

    public List<ObjectAdapter> process(final PersistenceQueryFindUsingApplibQueryDefault persistenceQuery) {
        final String queryName = persistenceQuery.getQueryName();
        final ObjectSpecification objectSpec = persistenceQuery.getSpecification();
        
        final List<?> results;
        if((objectSpec.getFullIdentifier() + "#pk").equals(queryName)) {
            results = getResultsPk(persistenceQuery);
        } else {
            results = getResults(persistenceQuery);
        }
        
        return loadAdapters(objectSpec, results);
    }

    // special case handling
    private List<?> getResultsPk(final PersistenceQueryFindUsingApplibQueryDefault persistenceQuery) {

        final String queryName = persistenceQuery.getQueryName();
        final Map<String, Object> map = unwrap(persistenceQuery.getArgumentsAdaptersByParameterName());
        final ObjectSpecification objectSpec = persistenceQuery.getSpecification();

        final Class<?> cls = objectSpec.getCorrespondingClass();
        if(!JdoPropertyUtils.hasPrimaryKeyProperty(objectSpec)) {
            throw new UnsupportedOperationException("cannot search by primary key for DataStore-assigned entities");
        }
        final OneToOneAssociation pkOtoa = JdoPropertyUtils.getPrimaryKeyPropertyFor(objectSpec);
        final String pkOtoaId = pkOtoa.getId();
        final String filter = pkOtoaId + "==" + map.get(pkOtoaId);
        final Query jdoQuery = getPersistenceManager().newQuery(cls, filter);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("query: " + queryName + ", filter: " + filter);
        }
        
        return (List<?>) jdoQuery.execute();
    }

    private List<?> getResults(final PersistenceQueryFindUsingApplibQueryDefault persistenceQuery) {
        
        final String queryName = persistenceQuery.getQueryName();
        final Map<String, Object> argumentsByParameterName = unwrap(persistenceQuery.getArgumentsAdaptersByParameterName());
        final QueryCardinality cardinality = persistenceQuery.getCardinality();
        final ObjectSpecification objectSpec = persistenceQuery.getSpecification();

        final PersistenceManager persistenceManager = getJdoObjectStore().getPersistenceManager();
        final Class<?> cls = objectSpec.getCorrespondingClass();
        final Query jdoQuery = persistenceManager.newNamedQuery(cls, queryName);
        if(persistenceQuery.hasRange()) {
            jdoQuery.setRange(persistenceQuery.getStart(), persistenceQuery.getEnd());
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("query: " + queryName + ", args: " + argumentsByParameterName);
        }
        
        final List<?> results = (List<?>) jdoQuery.executeWithMap(argumentsByParameterName);
        if (cardinality == QueryCardinality.MULTIPLE) {
            return results;
        }
        return results.isEmpty()?Collections.emptyList():results.subList(0, 1);
    }

    private static Map<String, Object> unwrap(final Map<String, ObjectAdapter> argumentAdaptersByParameterName) {
        final Map<String, Object> argumentsByParameterName = Maps.newHashMap();
        for (final String parameterName : argumentAdaptersByParameterName.keySet()) {
            final ObjectAdapter argumentAdapter = argumentAdaptersByParameterName.get(parameterName);
            final Object argument = ObjectAdapter.Util.unwrap(argumentAdapter);
            argumentsByParameterName.put(parameterName, argument);
        }
        return argumentsByParameterName;
    }


}

// Copyright (c) Naked Objects Group Ltd.
