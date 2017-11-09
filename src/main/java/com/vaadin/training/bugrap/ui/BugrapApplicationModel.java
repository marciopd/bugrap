package com.vaadin.training.bugrap.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.vaadin.bugrap.domain.BugrapRepository.ReportsQuery;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;
import com.vaadin.training.bugrap.ui.events.ReportSelectedEvent;
import com.vaadin.training.bugrap.ui.events.ReportUpdatedEvent;

public class BugrapApplicationModel {

	private static final List<Status> STATUS_LIST = Arrays.asList(Status.values());
	private static final List<Type> REPORT_TYPE_LIST = Arrays.asList(Type.values());
	private static final List<Priority> PRIORITY_LIST = Arrays.asList(Priority.values());

	private static final Comparator<Reporter> REPORTER_COMPARATOR = new Comparator<Reporter>() {
		@Override
		public int compare(final Reporter o1, final Reporter o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	private Project project;
	private ProjectVersion projectVersion;
	private Report report;

	public Project getProject() {
		return project;
	}

	public void setProject(final Project project) {
		this.project = project;
		UIEventBus.getInstance().publish(new ProjectChangedEvent(project));
	}

	public ProjectVersion getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(final ProjectVersion projectVersion) {
		this.projectVersion = projectVersion;
		UIEventBus.getInstance().publish(new ProjectVersionChangedEvent(projectVersion));
	}

	public Report getReport() {
		return report;
	}

	public void setReport(final Report report) {
		this.report = report;
		UIEventBus.getInstance().publish(new ReportSelectedEvent(report));
	}

	public List<Project> listProjects() {
		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		Collections.sort(projects);
		return projects;
	}

	public List<ProjectVersion> listProjectVersions() {

		if (project == null) {
			return Collections.emptyList();
		}

		final List<ProjectVersion> projectVersions = new ArrayList<>(
				BugrapFacade.getInstance().findProjectVersions(project));
		Collections.sort(projectVersions);
		return projectVersions;
	}

	public List<Report> listReports() {
		final ReportsQuery query = new ReportsQuery();
		query.project = project;
		query.projectVersion = projectVersion;

		return new ArrayList<>(BugrapFacade.getInstance().findReports(query));
	}

	public boolean isAllVersionsSelected() {
		return projectVersion == null;
	}

	public String getAssignedTo(final Reporter reporter) {
		if (reporter == null) {
			return null;
		}
		return reporter.getName();
	}

	public boolean isSingleReportSelected() {
		return report != null;
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

	public void updateSelectedReport() {
		BugrapFacade.getInstance().save(report);
		UIEventBus.getInstance().publish(new ReportUpdatedEvent(report));
	}

}
