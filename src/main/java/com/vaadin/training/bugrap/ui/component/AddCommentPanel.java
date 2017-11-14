package com.vaadin.training.bugrap.ui.component;

import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.model.AddCommentApplicationModel;
import com.vaadin.training.bugrap.ui.model.Models;

public class AddCommentPanel extends AddCommentPanelDesign {

	private static final long serialVersionUID = 101936797233120615L;

	private static final String EMPTY_STRING = "";

	public AddCommentPanel() {
		initCommentTextArea();
		initDoneButton();
		initCancelButton();
	}

	private void initCancelButton() {
		cancelButton.addClickListener(event -> {
			getApplicationModel().reset();
			clearInputFields();
		});
	}

	private void initDoneButton() {
		doneButton.setEnabled(false);
		doneButton.addClickListener(event -> {
			getApplicationModel().saveComment();
			clearInputFields();
		});
	}

	private void initCommentTextArea() {
		commentTextArea.addValueChangeListener(event -> {
			final String comment = event.getValue();
			getApplicationModel().setCommentText(comment);
			if (comment != null && !comment.isEmpty()) {
				doneButton.setEnabled(true);
			} else {
				doneButton.setEnabled(false);
			}
		});
	}

	public void initialize(final Reporter author, final Report report) {

		final AddCommentApplicationModel applicationModel = getApplicationModel();
		applicationModel.reset();
		applicationModel.setAuthor(author);
		applicationModel.setReport(report);

		clearInputFields();
	}

	private void clearInputFields() {
		commentTextArea.setValue(EMPTY_STRING);
	}

	private AddCommentApplicationModel getApplicationModel() {
		AddCommentApplicationModel applicationModel = UIScope.getCurrent().getProperty(Models.ADD_COMMENT_PANEL_MODEL);
		if (applicationModel == null) {
			applicationModel = new AddCommentApplicationModel();
			UIScope.getCurrent().setProperty(Models.ADD_COMMENT_PANEL_MODEL, applicationModel);

		}
		return applicationModel;

	}
}
