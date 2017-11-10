package com.vaadin.training.bugrap.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ReportsOverview extends ReportsOverviewDesign implements View {

	private static final long serialVersionUID = -6476318052860674057L;

	public ReportsOverview() {
		super();
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		reportView.setReportFromRequestParam();
	}

}
