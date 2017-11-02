package com.vaadin.training.bugrap.eventbus;

import com.vaadin.training.bugrap.scope.Scope;
import com.vaadin.training.bugrap.scope.ScopeHolder;
import com.vaadin.ui.UI;

public class UIEventBus {

	private UIEventBus() {
	}

	public static EventBus getInstance() {
		final Scope uiScope = ((ScopeHolder) UI.getCurrent()).getScope();
		final EventBus eventBus = uiScope.getProperty(UIEventBus.class.getName());
		if (eventBus != null) {
			return eventBus;
		}

		return createEventBusSafely(uiScope);
	}

	private static synchronized EventBus createEventBusSafely(final Scope uiScope) {
		EventBus eventBus = uiScope.getProperty(UIEventBus.class.getName());
		if (eventBus == null) {
			eventBus = new EventBus();
			uiScope.setProperty(UIEventBus.class.getName(), eventBus);
		}
		return eventBus;
	}
}
