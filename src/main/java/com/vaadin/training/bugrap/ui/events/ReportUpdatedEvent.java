package com.vaadin.training.bugrap.ui.events;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportUpdatedEvent {

	private final Report report;

	public ReportUpdatedEvent(final Report report) {
		this.report = report;
	}

	public Report getReport() {
		return report;
	}

}
