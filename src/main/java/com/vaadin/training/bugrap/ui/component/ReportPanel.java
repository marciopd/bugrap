package com.vaadin.training.bugrap.ui.component;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.ReportsOverviewUI;
import com.vaadin.training.bugrap.ui.events.CommentAddedEvent;
import com.vaadin.training.bugrap.ui.events.ReportsSelectedEvent;
import com.vaadin.training.bugrap.ui.model.Models;
import com.vaadin.training.bugrap.ui.model.ReportPanelModel;
import com.vaadin.training.bugrap.ui.model.UserModel;

public class ReportPanel extends ReportPanelDesign {

	private static final long serialVersionUID = 1790972443114757675L;
	private static final String REPORT_ID_PARAM = "reportId";
	private static final String FIRST_COMMENT_STYLE = "first-comment";

	private Binder<Report> reportBinder;

	public ReportPanel() {
		initUpdateButton();
		initRevertButton();
		initOpenWindowButton();
		UIEventBus.getInstance().subscribe(ReportsSelectedEvent.class, this::receiveReportSelectedEvent);
		UIEventBus.getInstance().subscribe(CommentAddedEvent.class, this::receiveCommentAddedEvent);
	}

	public void initialize() {

		final ReportPanelModel applicationModel = getApplicationModel();
		if (!applicationModel.isShowReportView()) {
			setVisible(false);
			return;
		}

		final Report report = applicationModel.getReport();
		reportSummaryLabel.setValue(report.getSummary());

		if (applicationModel.isMassModificationModeSelected()) {
			openNewWindowButton.setVisible(false);
			commentsPanel.setVisible(false);
			addCommentPanel.setVisible(false);
		} else {
			configOpenWindowReportId(applicationModel);
			openNewWindowButton.setVisible(true);
			initCommentsPanel();
			initAddCommentPanel();
		}

		initComboBoxes(applicationModel);
		initReportBinder(report);
		setVisible(true);
	}

	private void initAddCommentPanel() {
		addCommentPanel.initialize(getLoggedInUser(), getApplicationModel().getReport());
		addCommentPanel.setVisible(true);
	}

	private Reporter getLoggedInUser() {
		return UserModel.getInstance().getUser();
	}

	private void configOpenWindowReportId(final ReportPanelModel applicationModel) {
		final BrowserWindowOpener opener = (BrowserWindowOpener) openNewWindowButton.getExtensions().iterator().next();
		opener.setParameter(REPORT_ID_PARAM, String.valueOf(applicationModel.getReport().getId()));
	}

	private void initComboBoxes(final ReportPanelModel applicationModel) {
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

	private void initCommentsPanel() {
		final Report report = getApplicationModel().getReport();
		commentsLayout.removeAllComponents();

		final CommentPanel reportDescriptionComment = new CommentPanel(getName(report.getAuthor()), report.getReportedTimestamp(),
				report.getDescription());
		reportDescriptionComment.addStyleName(FIRST_COMMENT_STYLE);
		commentsLayout.addComponent(reportDescriptionComment);

		final List<Comment> reportComments = getApplicationModel().getReportComments();
		for (final Comment comment : reportComments) {
			commentsLayout.addComponent(new CommentPanel(comment));
		}

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

	private void initOpenWindowButton() {
		final BrowserWindowOpener opener = new BrowserWindowOpener(ReportsOverviewUI.class);
		opener.extend(openNewWindowButton);
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

	private ReportPanelModel getApplicationModel() {
		return UIScope.getCurrent().getProperty(Models.REPORT_PANEL_MODEL);
	}

	public void receiveReportSelectedEvent(final ReportsSelectedEvent reportSelectedEvent) {
		initialize();
	}

	public void receiveCommentAddedEvent(final CommentAddedEvent event) {
		commentsLayout.addComponent(new CommentPanel(event.getComment()));
		commentsPanel.setScrollTop(Integer.MAX_VALUE);
	}

	public void setReportFromRequestParam() {
		final String reportIdParam = VaadinRequest.getCurrent().getParameter(REPORT_ID_PARAM);
		getApplicationModel().setSelectReportById(reportIdParam);
	}
}
