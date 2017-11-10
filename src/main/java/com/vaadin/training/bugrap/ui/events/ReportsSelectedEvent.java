package com.vaadin.training.bugrap.ui.events;

import java.util.Set;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportsSelectedEvent {

	private final Set<Report> reports;

	public ReportsSelectedEvent(final Set<Report> selectedReports) {
		this.reports = selectedReports;
	}

	public Set<Report> getReports() {
		return reports;
	}
}
