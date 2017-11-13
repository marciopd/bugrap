package com.vaadin.training.bugrap.ui;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.scope.Scope;
import com.vaadin.training.bugrap.scope.ScopeHolder;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.model.MainViewModel;
import com.vaadin.training.bugrap.ui.model.Models;
import com.vaadin.training.bugrap.ui.model.ReportViewModel;
import com.vaadin.training.bugrap.ui.model.UserModel;
import com.vaadin.training.bugrap.ui.view.LoginForm;
import com.vaadin.training.bugrap.ui.view.MainView;
import com.vaadin.training.bugrap.ui.view.Views;
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
public class MainUI extends UI implements ScopeHolder {

	/** serial. */
	private static final long serialVersionUID = 6297796943026028324L;

	private final UIScope scope = new UIScope();

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		initializeModel();
		initializeViews();
	}

	private void initializeModel() {
		scope.setProperty(Models.MAIN_VIEW_MODEL, new MainViewModel());
		scope.setProperty(Models.REPORT_VIEW_MODEL, new ReportViewModel());
	}

	private void initializeViews() {
		final Navigator navigator = new Navigator(this, this);
		navigator.addView(Views.LOGIN, LoginForm.class);
		navigator.addView(Views.MAIN, MainView.class);

		if (UserModel.getInstance().isUserLoggedIn()) {
			navigator.navigateTo(Views.MAIN);
		} else {
			navigator.navigateTo(Views.LOGIN);
		}
	}

	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
	public static class BugrapUIServlet extends VaadinServlet {

		private static final long serialVersionUID = -7828539572500029539L;

		@PostConstruct
		public void initReporterDatabase() {
			BugrapFacade.getInstance().populateWithTestData();
		}

	}

	@Override
	public Scope getScope() {
		return scope;
	}

}
