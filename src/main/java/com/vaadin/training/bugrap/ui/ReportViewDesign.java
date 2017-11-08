package com.vaadin.training.bugrap.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ReportViewDesign extends VerticalLayout {
	protected Button openNewWindowButton;
	protected Label reportSummaryLabel;
	protected NativeSelect<org.vaadin.bugrap.domain.entities.Report.Priority> prioritySelect;
	protected NativeSelect<org.vaadin.bugrap.domain.entities.Report.Type> bugSelect;
	protected NativeSelect<org.vaadin.bugrap.domain.entities.Report.Status> statusSelect;
	protected NativeSelect<org.vaadin.bugrap.domain.entities.Reporter> assignedToSelect;
	protected NativeSelect<org.vaadin.bugrap.domain.entities.ProjectVersion> versionSelect;
	protected Button updateButton;
	protected Button revertButton;
	protected Label commentsLabel;

	public ReportViewDesign() {
		Design.read(this);
	}
}