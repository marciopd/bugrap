package com.vaadin.training.bugrap.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.alump.searchdropdown.SimpleSearchDropDown;
import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.view.MainViewDesign;
import com.vaadin.training.bugrap.view.Views;

public class MainView extends MainViewDesign implements View {

	private static final long serialVersionUID = 6316325087699578693L;
	private static final float SEARCH_REPORTS_DROPDOWN_WIDTH = 312f;

	@Override
	public void enter(final ViewChangeEvent event) {

		username.setCaption(UserController.getInstance().getUsername());

		logoutButton.addClickListener(clickEvent -> {
			UserController.getInstance().logout();
			getUI().getNavigator().navigateTo(Views.LOGIN);
		});

		initProjectSelect();

		initSearchReportsDropdown();
	}

	private void initProjectSelect() {
		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		Collections.sort(projects);
		projectSelect.setItems(projects);
	}

	private void initSearchReportsDropdown() {
		final SimpleSearchDropDown searchReportsDropDown = new SimpleSearchDropDown(query -> {
			return Collections.emptyList();
		});
		searchReportsDropDown.setPlaceHolder(Messages.SEARCH_REPORTS);
		searchReportsDropDown.setWidth(SEARCH_REPORTS_DROPDOWN_WIDTH, Unit.PIXELS);
		headerSecondLine.addComponent(searchReportsDropDown);
	}

}
