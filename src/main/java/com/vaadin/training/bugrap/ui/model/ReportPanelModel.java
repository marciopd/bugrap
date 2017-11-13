package com.vaadin.training.bugrap.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;
import com.vaadin.training.bugrap.ui.events.ReportsSelectedEvent;
import com.vaadin.training.bugrap.ui.events.ReportsUpdatedEvent;

public class ReportPanelModel {

	private static final int ONE = 1;
	private static final String MASS_MODE_REPORT_TITLE = "%d reports selected";
	private static final List<Status> STATUS_LIST = Arrays.asList(Status.values());
	private static final List<Type> REPORT_TYPE_LIST = Arrays.asList(Type.values());
	private static final List<Priority> PRIORITY_LIST = Arrays.asList(Priority.values());

	private static final Comparator<Reporter> REPORTER_COMPARATOR = new Comparator<Reporter>() {
		@Override
		public int compare(final Reporter o1, final Reporter o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	private Report report;
	private Set<Report> reports;
	private Report massModificationReport;

	public ReportPanelModel() {
		final EventBus eventBus = UIEventBus.getInstance();
		eventBus.subscribe(ProjectChangedEvent.class, this::receiveProjectChangedEvent);
		eventBus.subscribe(ProjectVersionChangedEvent.class, this::receiveVersionChangedEvent);
		eventBus.subscribe(ReportsSelectedEvent.class, this::receiveReportsSelectedEvent);
	}

	public void receiveReportsSelectedEvent(final ReportsSelectedEvent event) {
		setSelectedReports(event.getReports());
	}

	public void receiveProjectChangedEvent(final ProjectChangedEvent event) {
		setSelectedReports(null);
	}

	public void receiveVersionChangedEvent(final ProjectVersionChangedEvent event) {
		setSelectedReports(null);
	}

	public Report getReport() {
		if (isSingleModificationModeSelected()) {
			return report;
		}

		return massModificationReport;
	}

	public void updateSelectedReport() {
		if (isSingleModificationModeSelected()) {
			report = BugrapFacade.getInstance().save(report);
			UIEventBus.getInstance().publish(new ReportsUpdatedEvent(report));
		} else {
			for (final Report report : reports) {
				setPropertyIfNotNull(massModificationReport::getPriority, report::setPriority);
				setPropertyIfNotNull(massModificationReport::getType, report::setType);
				setPropertyIfNotNull(massModificationReport::getStatus, report::setStatus);
				setPropertyIfNotNull(massModificationReport::getAssigned, report::setAssigned);
				setPropertyIfNotNull(massModificationReport::getVersion, report::setVersion);
				BugrapFacade.getInstance().save(report);
			}
			UIEventBus.getInstance().publish(new ReportsUpdatedEvent(reports));
		}
	}

	public void setSelectedReports(final Set<Report> selectedReports) {
		if (selectedReports == null || selectedReports.isEmpty()) {
			setSingleModificationMode(null);
			return;
		}

		if (selectedReports.size() == ONE) {
			setSingleModificationMode(selectedReports.iterator().next());
		} else {
			setMassModificationMode(selectedReports);
		}
	}

	private void setSingleModificationMode(final Report report) {
		this.report = report;
		this.reports = null;
		this.massModificationReport = null;
	}

	private void setMassModificationMode(final Set<Report> reports) {
		this.reports = reports;
		report = null;
		massModificationReport = new Report();
		massModificationReport.setProject(reports.iterator().next().getProject());
		massModificationReport.setSummary(String.format(MASS_MODE_REPORT_TITLE, reports.size()));
		setPropertyIfUniqueValue(reports, Report::getPriority, massModificationReport::setPriority);
		setPropertyIfUniqueValue(reports, Report::getType, massModificationReport::setType);
		setPropertyIfUniqueValue(reports, Report::getStatus, massModificationReport::setStatus);
		setPropertyIfUniqueValue(reports, Report::getAssigned, massModificationReport::setAssigned);
		setPropertyIfUniqueValue(reports, Report::getVersion, massModificationReport::setVersion);
	}

	public boolean isSingleModificationModeSelected() {
		return report != null;
	}

	public boolean isMassModificationModeSelected() {
		return reports != null;
	}

	public List<ProjectVersion> listProjectVersions() {

		if (getReport() == null) {
			return Collections.emptyList();
		}

		final List<ProjectVersion> projectVersions = new ArrayList<>(
				BugrapFacade.getInstance().findProjectVersions(getReport().getProject()));
		Collections.sort(projectVersions);
		return projectVersions;
	}

	public List<Priority> listPriorities() {
		return PRIORITY_LIST;
	}

	public List<Type> listBugTypes() {
		return REPORT_TYPE_LIST;
	}

	public List<Status> listStatus() {
		return STATUS_LIST;
	}

	public List<Reporter> listReporters() {
		final List<Reporter> reporters = new ArrayList<>(BugrapFacade.getInstance().findReporters());
		Collections.sort(reporters, REPORTER_COMPARATOR);
		return reporters;
	}

	public boolean isShowReportView() {
		return isSingleModificationModeSelected() || isMassModificationModeSelected();
	}

	private <R> void setPropertyIfUniqueValue(final Collection<Report> reports,
			final Function<Report, R> propertyGetter, final Consumer<R> propertySetter) {

		final R sameResult = propertyGetter.apply(reports.iterator().next());

		for (final Report report : reports) {
			final R result = propertyGetter.apply(report);
			if (sameResult != result && (sameResult == null || result == null || !sameResult.equals(result))) {
				return;
			}
		}

		propertySetter.accept(sameResult);
	}

	private <R> void setPropertyIfNotNull(final Supplier<R> propertyGetter, final Consumer<R> propertySetter) {
		final R value = propertyGetter.get();
		if (value != null) {
			propertySetter.accept(value);
		}
	}

	public void setSelectReportById(final String reportIdParam) {
		final Long reportId = Long.valueOf(reportIdParam);
		if (reportId == null) {
			throw new IllegalArgumentException(String.format("Invalid reportId param: %s.", reportIdParam));
		}

		final Report report = BugrapFacade.getInstance().getReportById(reportId);
		if (report == null) {
			throw new IllegalArgumentException(String.format("Report ID not found: %d.", reportId));
		}

		final Set<Report> selectedReports = new HashSet<>(ONE);
		selectedReports.add(report);
		UIEventBus.getInstance().publish(new ReportsSelectedEvent(selectedReports));
	}

	public void addComment() {
		// TODO Auto-generated method stub

	}

	public List<Comment> getReportComments() {
		if (report == null) {
			return Collections.emptyList();
		}

		return BugrapFacade.getInstance().findComments(report);
	}
}
