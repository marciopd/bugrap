package com.vaadin.training.bugrap.ui.events;

import org.vaadin.bugrap.domain.entities.Project;

public class ProjectChangedEvent {

	private final Project selectedItem;

	public ProjectChangedEvent(final Project selectedItem) {
		this.selectedItem = selectedItem;
	}

	public Project getSelectedItem() {
		return selectedItem;
	}

}
