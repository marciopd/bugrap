package com.vaadin.training.bugrap.eventbus;

public interface Subscriber<T> {

	void receive(T event);

}
