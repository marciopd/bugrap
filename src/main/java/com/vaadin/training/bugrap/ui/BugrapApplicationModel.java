package com.vaadin.training.bugrap.ui;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
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

}
