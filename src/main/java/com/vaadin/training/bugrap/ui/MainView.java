package com.vaadin.training.bugrap.ui;

import java.util.Collections;
import java.util.List;

import org.vaadin.alump.searchdropdown.SimpleSearchDropDown;
import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
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
		projectVersionsSelect.setValue(null);
		final BugrapApplicationModel applicationModel = getApplicationModel();
		projectVersionsSelect.setItems(applicationModel.listProjectVersions());
		applicationModel.setProjectVersion(null);
	}

	private BugrapApplicationModel getApplicationModel() {
		return UIScope.getCurrent().getProperty(Models.BUGRAP_MODEL);
	}

	private void initProjectSelect() {
		final BugrapApplicationModel applicationModel = getApplicationModel();
		final List<Project> projects = applicationModel.listProjects();
		projectSelect.setItems(projects);
		projectSelect.addSelectionListener(event -> {
			applicationModel.setProject(event.getValue());
		});
		applicationModel.setProject(projects.get(FIRST));
		projectSelect.setValue(applicationModel.getProject());
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
