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


package org.apache.isis.viewer.dnd.view.content;

import org.apache.isis.viewer.dnd.view.Content;
import org.apache.isis.viewer.dnd.view.UserActionSet;


public abstract class AbstractContent implements Content {
    public void contentMenuOptions(final UserActionSet options) {}

    public boolean isCollection() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPersistable() {
        return false;
    }

    public boolean isTextParseable() {
        return false;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String windowTitle() {
        return "";
    }
}