package com.vaadin.training.bugrap.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;

public class BugrapApplicationModel {

	private Project project;

	private ProjectVersion projectVersion;

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

}
