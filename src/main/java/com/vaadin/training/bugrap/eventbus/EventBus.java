package com.vaadin.training.bugrap.eventbus;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {

	private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());
	private static final String MSG_SUBSCRIBER_EXECUTION_FAILED = "Subscriber execution failed.";
	private static final int SUBSCRIBERS_LIST_INITIAL_SIZE = 3;

	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<Class, Set<Subscriber>> subscribersPerEventType = new ConcurrentHashMap<>();

	@SuppressWarnings("rawtypes")
	public <T> void subscribe(final Class<T> eventType, final Subscriber<T> s) {
		Set<Subscriber> subscribers = subscribersPerEventType.get(eventType);
		if (subscribers == null) {
			subscribers = createSubscribersListSafely(eventType);
			subscribersPerEventType.put(eventType, subscribers);
		}
		subscribers.add(s);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> void publish(final T event) {

		final Set<Subscriber> subscribers = subscribersPerEventType.get(event.getClass());
		if (subscribers == null) {
			return;
		}

		for (final Subscriber subscriber : subscribers) {
			try {
				subscriber.receive(event);
			} catch (final RuntimeException e) {
				LOGGER.log(Level.SEVERE, MSG_SUBSCRIBER_EXECUTION_FAILED, e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private synchronized <T> Set<Subscriber> createSubscribersListSafely(final Class<T> event) {
		Set<Subscriber> subscribers = subscribersPerEventType.get(event);
		if (subscribers == null) {
			subscribers = new LinkedHashSet<>(SUBSCRIBERS_LIST_INITIAL_SIZE);
		}
		return subscribers;
	}

	public void removeAllSubscribers() {
		subscribersPerEventType.clear();
	}

}
