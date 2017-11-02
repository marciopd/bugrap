package com.vaadin.training.bugrap.scope;

public interface Scope {

	<T> T getProperty(String name);

	void setProperty(String name, Object value);

}
