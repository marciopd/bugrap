package com.vaadin.training.bugrap.ui;

import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.util.CommentFormat;
import com.vaadin.ui.Button.ClickListener;

public class ReportView extends ReportViewDesign implements View {

	private static final long serialVersionUID = 1790972443114757675L;

	private Binder<Report> reportBinder;
	private ClickListener updateButtonListener;
	private ClickListener revertButtonListener;

	@Override
	public void enter(final ViewChangeEvent event) {
		initialize();
	}

	public void initialize() {
		final BugrapApplicationModel applicationModel = getApplicationModel();

		final Report report = applicationModel.getReport();
		reportSummaryLabel.setValue(report.getSummary());

		if (applicationModel.isMassModificationModeSelected()) {
			openNewWindowButton.setVisible(false);
			commentsLabel.setVisible(false);
		} else {
			initComments(report);
		}

		initComboBoxes(applicationModel);
		initReportBinder(report);

		initUpdateButton();
		initRevertButton();
	}

	private void initComboBoxes(final BugrapApplicationModel applicationModel) {
		prioritySelect.clear();
		prioritySelect.setItems(applicationModel.listPriorities());

		bugSelect.clear();
		bugSelect.setItems(applicationModel.listBugTypes());

		statusSelect.clear();
		statusSelect.setItems(applicationModel.listStatus());

		assignedToSelect.clear();
		assignedToSelect.setItems(applicationModel.listReporters());

		versionSelect.clear();
		versionSelect.setItems(applicationModel.listProjectVersions());
	}

	private void initComments(final Report report) {
		final String reportDescriptionComment = CommentFormat.format(getName(report.getAuthor()), report.getReportedTimestamp(),
				report.getDescription());
		commentsLabel.setValue(reportDescriptionComment);
		commentsLabel.setVisible(true);
	}

	private void initRevertButton() {
		if (revertButtonListener == null) {
			revertButtonListener = event -> {
				reportBinder.readBean(getApplicationModel().getReport());
			};
		}
		revertButton.addClickListener(revertButtonListener);
	}

	private void initUpdateButton() {
		if (updateButtonListener == null) {
			updateButtonListener = event -> {
				try {
					reportBinder.writeBean(getApplicationModel().getReport());
				} catch (final ValidationException e) {
					throw new IllegalStateException("Unexpected validation error.", e);
				}
				getApplicationModel().updateSelectedReport();
			};
			updateButton.addClickListener(updateButtonListener);
		}
	}

	private void initReportBinder(final Report report) {
		if (reportBinder == null) {
			reportBinder = new Binder<Report>(Report.class);
			reportBinder.forField(this.prioritySelect).bind("priority");
			reportBinder.forField(this.bugSelect).bind("type");
			reportBinder.forField(this.statusSelect).bind("status");
			reportBinder.forField(this.assignedToSelect).bind("assigned");
			reportBinder.forField(this.versionSelect).bind("version");
		}
		reportBinder.readBean(report);
	}

	private String getName(final Reporter author) {
		if (author == null) {
			return null;
		}
		return author.getName();
	}

	private BugrapApplicationModel getApplicationModel() {
		return UIScope.getCurrent().getProperty(Models.BUGRAP_MODEL);
	}
}
