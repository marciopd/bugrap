package com.vaadin.training.bugrap.presenter;

import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.view.LoginFormDesign;
import com.vaadin.training.bugrap.view.Views;
import com.vaadin.ui.Notification;

public class LoginForm extends LoginFormDesign implements View {

	private static final long serialVersionUID = -1911579983152292406L;
	private static final String EMPTY_STRING = "";

	@Override
	public void enter(final ViewChangeEvent event) {
		initialize();
	}

	public void initialize() {
		resetValueAllFields();
		initLoginButton();
		initClearButton();
	}

	private void initClearButton() {
		clearButton.addClickListener(event -> {
			resetValueAllFields();
		});
		clearButton.setClickShortcut(KeyCode.ESCAPE);
	}

	private void initLoginButton() {
		loginButton.addClickListener(event -> {
			final String username = usernameText.getValue();
			final StringBuilder errorMessageBuilder = new StringBuilder();
			if (username.isEmpty()) {
				errorMessageBuilder.append(Messages.USERNAME_REQUIRED);
			}

			final String password = passwordText.getValue();
			if (password.isEmpty()) {
				errorMessageBuilder.append(Messages.PASSWORD_REQUIRED);
			}

			if (errorMessageBuilder.length() > 0) {
				errorLabel.setValue(errorMessageBuilder.toString());
				return;
			}

			final Reporter reporter = BugrapFacade.getInstance().authenticate(username, password);
			if (reporter == null) {
				Notification.show(Messages.LOGIN_FAILED, Notification.Type.ERROR_MESSAGE);
				return;
			}

			loginSucceeded(reporter);

		});
		loginButton.setClickShortcut(KeyCode.ENTER);
	}

	private void loginSucceeded(final Reporter reporter) {
		final String welcomeMessage = String.format(Messages.LOGIN_SUCCEEDED, reporter.getName());
		Notification.show(welcomeMessage, Notification.Type.HUMANIZED_MESSAGE);
		UserController.getInstance().setUser(reporter);
		resetValueAllFields();
		getUI().getNavigator().navigateTo(Views.MAIN);
	}

	private void resetValueAllFields() {
		usernameText.setValue(EMPTY_STRING);
		passwordText.setValue(EMPTY_STRING);
		errorLabel.setValue(EMPTY_STRING);
	}

}
