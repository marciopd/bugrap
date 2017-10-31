package com.vaadin.training.bugrap.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.view.MainViewDesign;

public class MainView extends MainViewDesign implements View {

	private static final long serialVersionUID = 6316325087699578693L;

	private static final String APP_HOME_PATH = "/";
	private static final String MSG_ERROR_REDIRECTING_HOME = "Error redirecting to home after logout.";

	@Override
	public void enter(final ViewChangeEvent event) {

		username.setCaption(UserController.getInstance().getUsername());

		logoutButton.addClickListener(clickEvent -> {
			UserController.getInstance().logout();
			VaadinSession.getCurrent().getSession().invalidate();
			sendRedirectHome();
		});

		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		projectSelect.setItems(getProjectNames(projects));
	}

	private void sendRedirectHome() {
		try {
			VaadinServletResponse.getCurrent().sendRedirect(APP_HOME_PATH);
		} catch (final IOException e) {
			throw new IllegalStateException(MSG_ERROR_REDIRECTING_HOME, e);
		}
	}

	private Collection<String> getProjectNames(final List<Project> projects) {
		final List<String> names = new ArrayList<>();
		for (final Project project : projects) {
			names.add(project.getName());
		}
		Collections.sort(names);
		return names;
	}
}
