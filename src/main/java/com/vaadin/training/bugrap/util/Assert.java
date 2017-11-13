package com.vaadin.training.bugrap.util;

import java.util.function.Supplier;

public class Assert {

	private Assert() {
	}

	public static <T> void notNull(final Supplier<T> getter, final String errorMessage) {
		if (getter.get() == null) {
			throw new IllegalStateException(errorMessage);
		}
	}
}
