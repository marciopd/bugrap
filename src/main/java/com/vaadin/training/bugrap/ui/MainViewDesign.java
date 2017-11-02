package com.vaadin.training.bugrap.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalSplitPanel;
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
public class MainViewDesign extends VerticalSplitPanel {
	protected ComboBox<org.vaadin.bugrap.domain.entities.Project> projectSelect;
	protected Label username;
	protected Button logoutButton;
	protected HorizontalLayout headerSecondLine;
	protected ComboBox<org.vaadin.bugrap.domain.entities.ProjectVersion> projectVersionsSelect;
	protected HorizontalLayout reportsGridLayout;

	public MainViewDesign() {
		Design.read(this);
	}
}
