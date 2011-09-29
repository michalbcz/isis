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

package org.apache.isis.tck.objstore.dflt.scalars;

import org.apache.isis.tck.dom.AbstractEntityRepository;
import org.apache.isis.tck.dom.scalars.PrimitiveValuedEntity;
import org.apache.isis.tck.dom.scalars.PrimitiveValuedEntityRepository;

public class PrimitiveValuedEntityRepositoryDefault extends AbstractEntityRepository<PrimitiveValuedEntity> implements PrimitiveValuedEntityRepository {

    public PrimitiveValuedEntityRepositoryDefault() {
        super(PrimitiveValuedEntity.class);
    }
    
    @Override
    public String getId() {
        return "primitiveValuedEntities";
    }

    /**
     * Required otherwise return type is erased
     */
    @Override
    public PrimitiveValuedEntity newEntity() {
        return super.newEntity();
    }

}