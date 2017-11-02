package com.vaadin.training.bugrap.ui.events;

import org.vaadin.bugrap.domain.entities.ProjectVersion;

public class ProjectVersionChangedEvent {

	private final ProjectVersion projectVersion;

	public ProjectVersionChangedEvent(final ProjectVersion projectVersion) {
		this.projectVersion = projectVersion;
	}

	public ProjectVersion getProjectVersion() {
		return projectVersion;
	}

}
