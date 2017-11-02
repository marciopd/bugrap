package com.vaadin.training.bugrap.scope;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.vaadin.ui.UI;

public class UIScope implements Scope {

	private final ConcurrentMap<String, Object> properties = new ConcurrentHashMap<>();

	public static Scope getCurrent() {
		return ((ScopeHolder) UI.getCurrent()).getScope();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String name) {
		return (T) properties.get(name);
	}

	@Override
	public void setProperty(final String name, final Object value) {
		properties.put(name, value);
	}

}
