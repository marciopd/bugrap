package com.vaadin.training.bugrap.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.view.MainViewDesign;
import com.vaadin.training.bugrap.view.Views;

public class MainView extends MainViewDesign implements View {

	private static final long serialVersionUID = 6316325087699578693L;

	@Override
	public void enter(final ViewChangeEvent event) {

		username.setCaption(UserController.getInstance().getUsername());

		logoutButton.addClickListener(clickEvent -> {
			UserController.getInstance().logout();
			getUI().getNavigator().navigateTo(Views.LOGIN);
		});

		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		Collections.sort(projects);
		projectSelect.setItems(projects);
	}

}
