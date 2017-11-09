package com.vaadin.training.bugrap.ui.events;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportSelectedEvent {

	private final Report report;

	public ReportSelectedEvent(final Report report) {
		this.report = report;
	}

	public Report getReport() {
		return report;
	}
}
