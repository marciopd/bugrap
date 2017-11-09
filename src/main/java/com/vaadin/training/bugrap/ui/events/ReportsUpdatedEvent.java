package com.vaadin.training.bugrap.ui.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.bugrap.domain.entities.Report;

public class ReportsUpdatedEvent {

	private static final int ONE = 1;
	private final Set<Report> reports;

	public ReportsUpdatedEvent(final Report report) {
		this.reports = new HashSet<>(ONE);
		this.reports.add(report);
	}

	public ReportsUpdatedEvent(final Set<Report> reports) {
		this.reports = reports;
	}

	public Set<Report> getReports() {
		if (reports == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(reports);
	}

}
