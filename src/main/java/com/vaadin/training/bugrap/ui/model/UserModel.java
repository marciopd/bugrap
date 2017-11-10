package com.vaadin.training.bugrap.ui.model;

import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.server.VaadinSession;

public class UserModel {

	private static final String EMPTY_STRING = "";
	private final static UserModel INSTANCE = new UserModel();

	private UserModel() {
	}

	public static UserModel getInstance() {
		return INSTANCE;
	}

	public Reporter getUser() {
		return (Reporter) VaadinSession.getCurrent().getAttribute(SessionAttributes.USER);
	}

	public boolean isUserLoggedIn() {
		return getUser() != null;
	}

	public void setUser(final Reporter reporter) {
		if (getUser() != null) {
			throw new IllegalStateException(Messages.USER_ALREADY_LOGGED);
		}
		VaadinSession.getCurrent().setAttribute(SessionAttributes.USER, reporter);
	}

	public void logout() {
		VaadinSession.getCurrent().setAttribute(SessionAttributes.USER, null);

	}

	public String getUsername() {
		final Reporter user = getUser();
		if (user == null) {
			return EMPTY_STRING;
		}
		return user.getName();
	}
}
