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


package org.apache.isis.extensions.wicket.ui.components.widgets.entitylink;

import org.apache.isis.extensions.wicket.model.common.NoResultsHandler;
import org.apache.isis.extensions.wicket.model.common.SelectionHandler;
import org.apache.isis.extensions.wicket.model.mementos.ActionMemento;
import org.apache.isis.extensions.wicket.model.mementos.ObjectAdapterMemento;
import org.apache.isis.extensions.wicket.model.models.ActionModel;
import org.apache.isis.extensions.wicket.model.models.ActionModel.SingleResultsMode;
import org.apache.isis.extensions.wicket.model.util.Actions;
import org.apache.isis.extensions.wicket.ui.components.widgets.cssmenu.CssMenuLinkFactory;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.feature.ObjectAction;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

final class FindUsingLinkFactory implements CssMenuLinkFactory {

	private static final long serialVersionUID = 1L;
	
	private final EntityLink entityLink;

	FindUsingLinkFactory(EntityLink entityLink) {
		this.entityLink = entityLink;
	}

	public LinkAndLabel newLink(ObjectAdapterMemento adapterMemento,
			ObjectAction action, final String linkId) {
		final ActionMemento actionMemento = new ActionMemento(action);
		final ActionModel.Mode actionMode = ActionModel.determineMode(action);
		final ActionModel actionModel = ActionModel.create(adapterMemento, actionMemento, actionMode, SingleResultsMode.SELECT);
		
		actionModel.setSelectionHandler(new SelectionHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSelected(Component context, ObjectAdapter selectedAdapter) {
				entityLink.onSelected(selectedAdapter);
			}
		});
		actionModel.setNoResultsHandler(new NoResultsHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onNoResults(Component context) {
				entityLink.onNoResults();
			}
		});

		return new LinkAndLabel(new Link<String>(linkId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				entityLink.onClick(actionModel);
			}
		}, Actions.labelFor(action));
	}
	
}