package com.vaadin.training.bugrap.ui;

import java.util.Collections;
import java.util.List;

import org.vaadin.alump.searchdropdown.SimpleSearchDropDown;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;
import com.vaadin.training.bugrap.ui.events.ReportSelectedEvent;
import com.vaadin.training.bugrap.ui.events.ReportUpdatedEvent;
import com.vaadin.training.bugrap.util.ElapsedTimeFormat;
import com.vaadin.training.bugrap.util.PriorityFormat;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.HtmlRenderer;

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
		initReportsGrid();
		initReportView();

		subscribeToEvents();
	}

	private void subscribeToEvents() {
		final EventBus eventBus = UIEventBus.getInstance();
		eventBus.subscribe(ProjectChangedEvent.class, this::receiveProjectChangedEvent);
		eventBus.subscribe(ProjectVersionChangedEvent.class, this::receiveVersionChangedEvent);
		eventBus.subscribe(ReportSelectedEvent.class, this::receiveReportSelectedEvent);
		eventBus.subscribe(ReportUpdatedEvent.class, this::receiveReportUpdatedEvent);
	}

	private void initReportsGrid() {

		final BugrapApplicationModel applicationModel = getApplicationModel();
		final List<Report> reports = applicationModel.listReports();

		if (!reports.isEmpty()) {
			reportsGrid.removeAllColumns();
			reportsGrid.setSizeFull();
			reportsGrid.setItems(reports);

			final GridSortOrderBuilder<Report> sortOrderBuilder = new GridSortOrderBuilder<>();
			if (applicationModel.isAllVersionsSelected()) {
				final Column<Report, ProjectVersion> projectVersionColumn = reportsGrid.addColumn(Report::getVersion).setCaption("VERSION");
				sortOrderBuilder.thenAsc(projectVersionColumn);
			}

			final Column<Report, String> priorityColumn = reportsGrid
					.addColumn(report -> PriorityFormat.getInstance().format(report.getPriority()), new HtmlRenderer())
					.setCaption("PRIORITY");
			sortOrderBuilder.thenDesc(priorityColumn);
			reportsGrid.addColumn(Report::getType).setCaption("TYPE");
			reportsGrid.addColumn(Report::getSummary).setCaption("SUMMARY");
			reportsGrid.addColumn(report -> applicationModel.getAssignedTo(report.getAssigned()))
					.setCaption("ASSIGNED TO");
			reportsGrid.addColumn(report -> ElapsedTimeFormat.format(report.getTimestamp()))
					.setCaption("LAST MODIFIED");
			reportsGrid.addColumn(report -> ElapsedTimeFormat.format(report.getReportedTimestamp()))
					.setCaption("REPORTED");

			reportsGrid.setSortOrder(sortOrderBuilder);

			reportsGrid.addSelectionListener(event -> {
				applicationModel.setReport(event.getFirstSelectedItem().orElse(null));
			});

			reportsGrid.setVisible(true);

		} else {
			reportsGrid.setVisible(false);
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

	private void initReportView() {
		reportView.setVisible(false);

		final BugrapApplicationModel applicationModel = getApplicationModel();
		if (applicationModel.isSingleReportSelected()) {
			reportView.initialize();
			reportView.setVisible(true);
		}
	}

	public void receiveProjectChangedEvent(final ProjectChangedEvent event) {
		initProjectVersions();
		initReportsGrid();
	}

	public void receiveVersionChangedEvent(final ProjectVersionChangedEvent event) {
		initReportsGrid();
	}

	public void receiveReportSelectedEvent(final ReportSelectedEvent reportSelectedEvent) {
		initReportView();
	}

	public void receiveReportUpdatedEvent(final ReportUpdatedEvent event) {
		initReportsGrid();
	}
}
