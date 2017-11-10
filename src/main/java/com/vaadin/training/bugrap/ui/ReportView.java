package com.vaadin.training.bugrap.ui;

import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.component.Comment;

public class ReportView extends ReportViewDesign {

	private static final long serialVersionUID = 1790972443114757675L;
	private static final String FIRST_COMMENT_STYLE = "first-comment";

	private Binder<Report> reportBinder;

	public ReportView() {
		initUpdateButton();
		initRevertButton();
	}

	public void initialize() {
		final BugrapApplicationModel applicationModel = getApplicationModel();

		final Report report = applicationModel.getReport();
		reportSummaryLabel.setValue(report.getSummary());

		if (applicationModel.isMassModificationModeSelected()) {
			openNewWindowButton.setVisible(false);
			commentsPanel.setVisible(false);
		} else {
			openNewWindowButton.setVisible(true);
			initComments(report);
		}

		initComboBoxes(applicationModel);
		initReportBinder(report);
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
		commentsLayout.removeAllComponents();

		final Comment reportDescriptionComment = new Comment(getName(report.getAuthor()), report.getReportedTimestamp(),
				report.getDescription());
		reportDescriptionComment.addStyleName(FIRST_COMMENT_STYLE);
		commentsLayout.addComponent(reportDescriptionComment);

		commentsPanel.setVisible(true);
	}

	private void initRevertButton() {
		revertButton.addClickListener(event -> {
			reportBinder.readBean(getApplicationModel().getReport());
		});
	}

	private void initUpdateButton() {
		updateButton.addClickListener(event -> {
			try {
				reportBinder.writeBean(getApplicationModel().getReport());
			} catch (final ValidationException e) {
				throw new IllegalStateException("Unexpected validation error.", e);
			}
			getApplicationModel().updateSelectedReport();
		});
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
