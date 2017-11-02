package com.vaadin.training.bugrap.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.alump.searchdropdown.SimpleSearchDropDown;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;

public class MainView extends MainViewDesign implements View {

	private static final long serialVersionUID = 6316325087699578693L;
	private static final float SEARCH_REPORTS_DROPDOWN_WIDTH = 340f;
	private static final int FIRST = 0;

	@Override
	public void enter(final ViewChangeEvent event) {

		username.setCaption(UserController.getInstance().getUsername());

		logoutButton.addClickListener(clickEvent -> {
			UserController.getInstance().logout();
			getUI().getNavigator().navigateTo(Views.LOGIN);
		});

		initProjectSelect();
		initSearchReportsDropdown();
		initProjectVersions();

	}

	private void initProjectVersions() {
		findProjectVersionsAndSelectEmpty();
		UIEventBus.getInstance().subscribe(ProjectChangedEvent.class, this::receive);
	}

	private void findProjectVersionsAndSelectEmpty() {
		final List<ProjectVersion> projectVersions = new ArrayList<>(
				BugrapFacade.getInstance().findProjectVersions(getApplicationModel().getProject()));
		Collections.sort(projectVersions);
		projectVersionsSelect.setValue(null);
		projectVersionsSelect.setItems(projectVersions);
		getApplicationModel().setProjectVersion(null);
	}

	private BugrapApplicationModel getApplicationModel() {
		return UIScope.getCurrent().getProperty(Models.BUGRAP_MODEL);
	}

	private void initProjectSelect() {
		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		Collections.sort(projects);
		projectSelect.setItems(projects);
		projectSelect.addSelectionListener(event -> {
			getApplicationModel().setProject(event.getValue());
		});
		getApplicationModel().setProject(projects.get(FIRST));
		projectSelect.setValue(getApplicationModel().getProject());
	}

	private void initSearchReportsDropdown() {
		final SimpleSearchDropDown searchReportsDropDown = new SimpleSearchDropDown(query -> {
			// TODO: implement real search
			return Collections.emptyList();
		});
		searchReportsDropDown.setPlaceHolder(Messages.SEARCH_REPORTS);
		searchReportsDropDown.setWidth(SEARCH_REPORTS_DROPDOWN_WIDTH, Unit.PIXELS);
		headerSecondLine.addComponent(searchReportsDropDown);
	}

	public void receive(final ProjectChangedEvent event) {
		findProjectVersionsAndSelectEmpty();
	}

}
