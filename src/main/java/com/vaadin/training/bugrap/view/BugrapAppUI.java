package com.vaadin.training.bugrap.view;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.presenter.LoginForm;
import com.vaadin.training.bugrap.presenter.MainView;
import com.vaadin.training.bugrap.presenter.UserController;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class BugrapAppUI extends UI {

	/** serial. */
	private static final long serialVersionUID = 6297796943026028324L;

	@Override
	protected void init(final VaadinRequest vaadinRequest) {

		final Navigator navigator = new Navigator(this, this);
		navigator.addView(Views.LOGIN, LoginForm.class);
		navigator.addView(Views.MAIN, MainView.class);

		if (UserController.getInstance().isUserLoggedIn()) {
			navigator.navigateTo(Views.MAIN);
		} else {
			navigator.navigateTo(Views.LOGIN);
		}

	}

	@WebServlet(urlPatterns = "/*", name = "BugrapUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = BugrapAppUI.class, productionMode = false)
	public static class BugrapUIServlet extends VaadinServlet {

		private static final long serialVersionUID = -7828539572500029539L;

		@PostConstruct
		public void initReporterDatabase() {
			BugrapFacade.getInstance().populateWithTestData();
		}

	}
}
