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


package org.apache.isis.viewer.wicket.ui.components.scalars.reference;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.scalars.ScalarPanelAbstract;
import org.apache.isis.viewer.wicket.ui.components.widgets.entitylink.EntityLink;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Panel for rendering scalars which of are of reference type (as opposed to value types).
 */
public class ReferencePanel extends ScalarPanelAbstract {

	private static final long serialVersionUID = 1L;
	
	private static final String ID_SCALAR_IF_REGULAR = "scalarIfRegular";
	private static final String ID_SCALAR_NAME = "scalarName";
	private static final String ID_FEEDBACK = "feedback";
	
	private static final String ID_SCALAR_IF_COMPACT = "scalarIfCompact";

	private EntityLink entityLink;


	public ReferencePanel(String id, final ScalarModel scalarModel) {
		super(id, scalarModel);
	}


	@Override
	protected void onBeforeRenderWhenEnabled() {
		super.onBeforeRenderWhenEnabled();
		entityLink.setEnabled(true);
		entityLink.syncFindUsingVisibility();
	}
	
	@Override
	protected void onBeforeRenderWhenViewMode() {
		super.onBeforeRenderWhenViewMode();
		entityLink.setEnabled(false);
		entityLink.syncFindUsingVisibility();
	}
	
	@Override
	protected void onBeforeRenderWhenDisabled(String disableReason) {
		super.onBeforeRenderWhenDisabled(disableReason);
		entityLink.setEnabled(false);
		entityLink.syncFindUsingVisibility();
	}

	protected FormComponentLabel addComponentForRegular() {
		ScalarModel scalarModel = getModel();
		String name = scalarModel.getName();

		
		entityLink = (EntityLink) getComponentFactoryRegistry().createComponent(ComponentType.ENTITY_LINK, getModel());
		
		entityLink.setOutputMarkupId(true);
		entityLink.setLabel(Model.of(name));

		FormComponentLabel labelIfRegular = new FormComponentLabel(ID_SCALAR_IF_REGULAR,entityLink);

		labelIfRegular.add(entityLink);
		Label scalarName = new Label(ID_SCALAR_NAME, getFormat().getLabelCaption(entityLink));
		labelIfRegular.add(scalarName);

		addOrReplace(labelIfRegular);
		
		addOrReplace(new ComponentFeedbackPanel(ID_FEEDBACK, entityLink));

		
		addStandardSemantics();
		addSemantics();

		return labelIfRegular;
	}


	protected void addStandardSemantics() {
		setRequiredIfSpecified();
	}

	private void setRequiredIfSpecified() {
		ScalarModel scalarModel = getModel();
		boolean required = scalarModel.isRequired();
		entityLink.setRequired(required);
	}

	protected void addSemantics() {
		
		addObjectAdapterValidator();
	}
	
	private void addObjectAdapterValidator() {
		final ScalarModel scalarModel = getModel();

		entityLink.add(new IValidator<ObjectAdapter>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<ObjectAdapter> validatable) {
				ObjectAdapter proposedAdapter = validatable.getValue();
				String reasonIfAny = scalarModel.validate(proposedAdapter);
				if (reasonIfAny != null) {
					ValidationError error = new ValidationError();
					error.setMessage(reasonIfAny);
					validatable.error(error);
				}
			}
		});
	}


	/**
	 * Mandatory hook method to build the component to render the model
	 * when in {@link Format#COMPACT compact} format.
	 */
	protected Component addComponentForCompact() {
		Label labelIfCompact = new Label(ID_SCALAR_IF_COMPACT, getModel().getObjectAsString());
		addOrReplace(labelIfCompact);
		return labelIfCompact;
	}


}