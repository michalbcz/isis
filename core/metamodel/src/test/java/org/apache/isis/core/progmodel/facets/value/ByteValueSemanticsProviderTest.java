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

package org.apache.isis.core.progmodel.facets.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.profiles.Localization;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetHolderImpl;
import org.apache.isis.core.metamodel.facets.object.parseable.TextEntryParseException;
import org.apache.isis.core.progmodel.facets.value.bytes.ByteValueSemanticsProviderAbstract;
import org.apache.isis.core.progmodel.facets.value.bytes.ByteWrapperValueSemanticsProvider;

public class ByteValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private ByteValueSemanticsProviderAbstract value;

    private Byte byteObj;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        byteObj = Byte.valueOf((byte) 102);
        allowMockAdapterToReturn(byteObj);
        holder = new FacetHolderImpl();

        context.checking(new Expectations() {
            {
                allowing(mockConfiguration).getString("isis.value.format.byte");
                will(returnValue(null));
            }
        });

        setValue(value = new ByteWrapperValueSemanticsProvider(holder, mockConfiguration, mockContext));
    }

    @Test
    public void testParseValidString() throws Exception {
        final Object parsed = value.parseTextEntry(null, "21", null);
        assertEquals(Byte.valueOf((byte) 21), parsed);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(null, "xs21z4xxx23", null);
            fail();
        } catch (final TextEntryParseException expected) {
        }
    }

    @Test
    public void testTitleOf() throws Exception {
        assertEquals("102", value.displayTitleOf(byteObj, (Localization) null));
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("102", value.toEncodedString(byteObj));
    }

    @Test
    public void testDecode() throws Exception {
        final Object parsed = value.fromEncodedString("-91");
        assertEquals(Byte.valueOf((byte) -91), parsed);
    }

}
