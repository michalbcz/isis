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

package org.apache.isis.core.progmodel.facets.propparam.multiline;

import java.lang.reflect.Method;

import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessClassContext;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessParameterContext;
import org.apache.isis.core.metamodel.facets.multiline.MultiLineFacet;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;
import org.apache.isis.core.progmodel.facets.object.multiline.annotation.MultiLineAnnotationOnTypeFacetFactory;
import org.apache.isis.core.progmodel.facets.object.multiline.annotation.MultiLineFacetAnnotationOnType;
import org.apache.isis.core.progmodel.facets.param.multiline.annotation.MultiLineAnnotationOnParameterFacetFactory;
import org.apache.isis.core.progmodel.facets.param.multiline.annotation.MultiLineFacetAnnotationOnParameter;
import org.apache.isis.core.progmodel.facets.properties.multiline.annotation.MultiLineOnPropertyFacetFactory;
import org.apache.isis.core.progmodel.facets.properties.multiline.annotation.MultiLineFacetOnPropertyAnnotation;

public class MultiLineAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    public void testMultiLineAnnotationPickedUpOnClass() {
        final MultiLineAnnotationOnTypeFacetFactory facetFactory = new MultiLineAnnotationOnTypeFacetFactory();

        @MultiLine(numberOfLines = 3, preventWrapping = false)
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetAnnotationOnType);
        final MultiLineFacetAnnotationOnType multiLineFacetAnnotation = (MultiLineFacetAnnotationOnType) facet;
        assertEquals(3, multiLineFacetAnnotation.numberOfLines());
        assertEquals(false, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationPickedUpOnProperty() {
        final MultiLineOnPropertyFacetFactory facetFactory = new MultiLineOnPropertyFacetFactory();

        class Customer {
            @SuppressWarnings("unused")
            @MultiLine(numberOfLines = 12, preventWrapping = true)
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(new ProcessMethodContext(Customer.class, null, null, method, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetOnPropertyAnnotation);
        final MultiLineFacetOnPropertyAnnotation multiLineFacetAnnotation = (MultiLineFacetOnPropertyAnnotation) facet;
        assertEquals(12, multiLineFacetAnnotation.numberOfLines());
        assertEquals(true, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationPickedUpOnActionParameter() {
        final MultiLineAnnotationOnParameterFacetFactory facetFactory = new MultiLineAnnotationOnParameterFacetFactory();

        class Customer {
            @SuppressWarnings("unused")
            public void someAction(@MultiLine(numberOfLines = 8, preventWrapping = false) final String foo) {
            }
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { String.class });

        facetFactory.processParams(new ProcessParameterContext(method, 0, facetedMethodParameter));

        final Facet facet = facetedMethodParameter.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetAnnotationOnParameter);
        final MultiLineFacetAnnotationOnParameter multiLineFacetAnnotation = (MultiLineFacetAnnotationOnParameter) facet;
        assertEquals(8, multiLineFacetAnnotation.numberOfLines());
        assertEquals(false, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationDefaults() {
        final MultiLineAnnotationOnTypeFacetFactory facetFactory = new MultiLineAnnotationOnTypeFacetFactory();

        @MultiLine
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(MultiLineFacet.class);
        final MultiLineFacetAnnotationOnType multiLineFacetAnnotationOnType = (MultiLineFacetAnnotationOnType) facet;
        assertEquals(6, multiLineFacetAnnotationOnType.numberOfLines());
        assertEquals(true, multiLineFacetAnnotationOnType.preventWrapping());
    }

    public void testMultiLineAnnotationIgnoredForNonStringProperties() {
        final MultiLineOnPropertyFacetFactory facetFactory = new MultiLineOnPropertyFacetFactory();

        class Customer {
            @SuppressWarnings("unused")
            @MultiLine(numberOfLines = 8, preventWrapping = false)
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method method = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(new ProcessMethodContext(Customer.class, null, null, method, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(MultiLineFacet.class);
        assertNull(facet);
    }

    public void testMultiLineAnnotationIgnoredForNonStringActionParameters() {
        final MultiLineAnnotationOnParameterFacetFactory facetFactory = new MultiLineAnnotationOnParameterFacetFactory();

        class Customer {
            @SuppressWarnings("unused")
            public void someAction(@MultiLine(numberOfLines = 8, preventWrapping = false) final int foo) {
            }
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(new ProcessParameterContext(method, 0, facetedMethodParameter));

        assertNull(facetedMethod.getFacet(MultiLineFacet.class));
    }

}
