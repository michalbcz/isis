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


package org.apache.isis.webapp.view.display;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.runtime.context.IsisContext;
import org.apache.isis.webapp.AbstractElementProcessor;
import org.apache.isis.webapp.ForbiddenException;
import org.apache.isis.webapp.ScimpiException;
import org.apache.isis.webapp.context.RequestContext.Scope;
import org.apache.isis.webapp.processor.Request;


public class TableCell extends AbstractElementProcessor {

    public void process(Request request) {
        String id = request.getOptionalProperty(OBJECT);
        String fieldName = request.getRequiredProperty(FIELD);
        ObjectAdapter object = request.getContext().getMappedObjectOrVariable(id, ELEMENT);
        ObjectAssociation field = object.getSpecification().getAssociation(fieldName);
        if (field == null) {
            throw new ScimpiException("No field " + fieldName + " in " + object.getSpecification().getFullName());
        }
        if (field.isVisible(IsisContext.getAuthenticationSession(), object).isVetoed()) {
            throw new ForbiddenException("Field " + fieldName + " in " + object + " is not visible");
        }
        request.appendHtml("<td>");
        ObjectAdapter fieldReference = field.get(object);
        String source = fieldReference == null ? "" : request.getContext().mapObject(fieldReference, Scope.REQUEST);
        String name = request.getOptionalProperty(RESULT_NAME, fieldName);
        request.getContext().addVariable(name, source, Scope.REQUEST);
        
        request.pushNewBuffer();
        request.processUtilCloseTag();
        String buffer = request.popBuffer();
        if (buffer.trim().length() == 0) {
            request.appendHtml( fieldReference == null ? "" : fieldReference.titleString());
        } else {
            request.appendHtml(buffer);
        }
        request.appendHtml("</td>");
    }

    public String getName() {
        return "table-cell";
    }

}
