package com.vaadin.training.bugrap.ui;

import java.util.Collections;
import java.util.List;

import org.vaadin.alump.searchdropdown.SimpleSearchDropDown;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;

import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

public class MainView extends MainViewDesign implements View {

	private static final long serialVersionUID = 6316325087699578693L;
	private static final float SEARCH_REPORTS_DROPDOWN_WIDTH = 340f;
	private static final int FIRST = 0;

	private Grid<Report> reportsGrid = new Grid<>();

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
		initReportsGrid();

		subscribeToEvents();
	}

	private void subscribeToEvents() {
		final EventBus eventBus = UIEventBus.getInstance();
		eventBus.subscribe(ProjectChangedEvent.class, this::receiveProjectChangedEvent);
		eventBus.subscribe(ProjectVersionChangedEvent.class, this::receiveVersionChangedEvent);
	}

	private void initReportsGrid() {

		reportsGridLayout.removeComponent(reportsGrid);

		final BugrapApplicationModel applicationModel = getApplicationModel();
		final List<Report> reports = applicationModel.listReports();

		if (!reports.isEmpty()) {
			reportsGrid = new Grid<Report>();
			reportsGrid.setSizeFull();
			reportsGrid.setItems(reports);

			final GridSortOrderBuilder<Report> sortOrderBuilder = new GridSortOrderBuilder<>();

			if (applicationModel.isAllVersionsSelected()) {
				final Column<Report, ProjectVersion> projectVersionColumn = reportsGrid.addColumn(Report::getVersion).setCaption("VERSION");
				sortOrderBuilder.thenAsc(projectVersionColumn);
			}

			final Column<Report, Priority> priorityColumn = reportsGrid.addColumn(Report::getPriority).setCaption("PRIORITY");
			sortOrderBuilder.thenDesc(priorityColumn);
			reportsGrid.addColumn(Report::getType).setCaption("TYPE");
			reportsGrid.addColumn(Report::getSummary).setCaption("SUMMARY");
			reportsGrid.addColumn(Report::getAssigned).setCaption("ASSIGNED TO");
			reportsGrid.addColumn(Report::getTimestamp).setCaption("LAST MODIFIED");
			reportsGrid.addColumn(Report::getReportedTimestamp).setCaption("REPORTED");

			reportsGrid.setSortOrder(sortOrderBuilder);

			reportsGridLayout.addComponent(reportsGrid);
		}

	}

	private void initProjectVersions() {
		projectVersionsSelect.setValue(null);
		final BugrapApplicationModel applicationModel = getApplicationModel();
		projectVersionsSelect.setItems(applicationModel.listProjectVersions());
		applicationModel.setProjectVersion(null);

		projectVersionsSelect.addSelectionListener(event -> {
			applicationModel.setProjectVersion(event.getValue());
		});
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

	public void receiveProjectChangedEvent(final ProjectChangedEvent event) {
		initProjectVersions();
		initReportsGrid();
	}

	public void receiveVersionChangedEvent(final ProjectVersionChangedEvent event) {
		initReportsGrid();
	}
}
