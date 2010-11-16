package org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.bdd.common.CellBinding;
import org.apache.isis.viewer.bdd.common.StoryBoundValueException;
import org.apache.isis.viewer.bdd.common.fixtures.perform.PerformContext;

public class Disabled extends ThatSubcommandAbstract {

	public Disabled() {
		super("is disabled", "is not usable", "is not enabled");
	}

	public ObjectAdapter that(final PerformContext performContext)
			throws StoryBoundValueException {

		if (performContext.usableMemberConsent().isAllowed()) {
			CellBinding onMemberBinding = performContext.getPeer()
					.getOnMemberBinding();
			throw StoryBoundValueException.current(onMemberBinding, "(usable)");
		}

		return null;

	}

}