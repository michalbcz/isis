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


package org.apache.isis.core.runtime.persistence.adapterfactory;

import org.apache.isis.core.commons.components.Injectable;
import org.apache.isis.core.commons.components.SessionScopedComponent;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.oid.Oid;


/**
 * Polymorphic instantiation of {@link ObjectAdapter}.
 * 
 * <p>
 * Introduced to allow subclasses of ObjectAdapter with support for {@link OneToOneAssociationInstance} 
 * and so on.
 */
public interface AdapterFactory extends SessionScopedComponent, Injectable {

    public ObjectAdapter createAdapter(final Object pojo, final Oid oid);
}