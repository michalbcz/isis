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


package org.apache.isis.core.runtime.persistence.oidgenerator.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.isis.core.metamodel.encoding.EncodabilityContractTest;
import org.apache.isis.core.runtime.persistence.oidgenerator.simple.SerialOid;

public abstract class SerialOidEncodabilityAbstractTest extends EncodabilityContractTest {

	@Override
	protected void assertRoundtripped(
			Object decodedEncodable,
			Object originalEncodable) {
		SerialOid decoded = (SerialOid) decodedEncodable;
		SerialOid original = (SerialOid) originalEncodable;
		
		assertThat(decoded.equals(original), is(true));
		assertThat(decoded.hasPrevious(), is(original.hasPrevious()));
		assertThat(decoded.getPrevious(), is(original.getPrevious()));
	}
}