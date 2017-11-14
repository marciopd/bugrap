package com.vaadin.training.bugrap.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.training.bugrap.scope.Scope;
import com.vaadin.training.bugrap.scope.ScopeHolder;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.model.Models;
import com.vaadin.training.bugrap.ui.model.ReportPanelModel;
import com.vaadin.training.bugrap.ui.model.UserModel;
import com.vaadin.training.bugrap.ui.view.LoginForm;
import com.vaadin.training.bugrap.ui.view.ReportsOverview;
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
public class ReportsOverviewUI extends UI implements ScopeHolder {

	/** serial. */
	private static final long serialVersionUID = 6297796943026028324L;

	private final UIScope scope = new UIScope();

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		final String reportId = vaadinRequest.getParameter("reportId");
		initializeModel(reportId);
		initializeViews();
	}

	private void initializeModel(final String reportIdParam) {
		final ReportPanelModel reportViewModel = new ReportPanelModel();
		reportViewModel.setSelectReportById(reportIdParam);
		scope.setProperty(Models.REPORT_PANEL_MODEL, reportViewModel);
	}

	private void initializeViews() {
		final Navigator navigator = new Navigator(this, this);
		navigator.addView(Views.LOGIN, LoginForm.class);
		navigator.addView(Views.REPORTS_OVERVIEW, ReportsOverview.class);

		if (UserModel.getInstance().isUserLoggedIn()) {
			navigator.navigateTo(Views.REPORTS_OVERVIEW);
		} else {
			navigator.navigateTo(Views.LOGIN);
		}
	}

	@Override
	public Scope getScope() {
		return scope;
	}

}
