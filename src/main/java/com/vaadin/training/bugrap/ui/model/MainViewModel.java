package com.vaadin.training.bugrap.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.vaadin.bugrap.domain.BugrapRepository.ReportsQuery;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;
import com.vaadin.training.bugrap.ui.events.ReportsSelectedEvent;

public class MainViewModel {

	private Project project;
	private ProjectVersion projectVersion;

	public Project getProject() {
		return project;
	}

	public void setProject(final Project project) {
		this.project = project;
		this.projectVersion = null;
		UIEventBus.getInstance().publish(new ProjectChangedEvent(project));
	}

	public ProjectVersion getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(final ProjectVersion projectVersion) {
		this.projectVersion = projectVersion;
		UIEventBus.getInstance().publish(new ProjectVersionChangedEvent(projectVersion));
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

	public void setSelectedReports(final Set<Report> selectedReports) {
		UIEventBus.getInstance().publish(new ReportsSelectedEvent(selectedReports));
	}

}
