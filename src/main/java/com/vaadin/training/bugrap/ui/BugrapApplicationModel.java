package com.vaadin.training.bugrap.ui;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.vaadin.bugrap.domain.BugrapRepository.ReportsQuery;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.ProjectChangedEvent;
import com.vaadin.training.bugrap.ui.events.ProjectVersionChangedEvent;

public class BugrapApplicationModel {

	private static final int ONE_MINUTE_IN_SECONDS = 60;

	private static final int ONE_HOUR_IN_SECONDS = 3600;

	private static final int ONE_WEEK_IN_DAYS = 7;

	private static final int ZERO = 0;

	private Project project;

	private ProjectVersion projectVersion;

	public Project getProject() {
		return project;
	}

	public void setProject(final Project project) {
		this.project = project;
		UIEventBus.getInstance().publish(new ProjectChangedEvent(project));
	}

	public ProjectVersion getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(final ProjectVersion projectVersion) {
		this.projectVersion = projectVersion;
		UIEventBus.getInstance().publish(new ProjectVersionChangedEvent(projectVersion));
	}

	public List<Project> listProjects() {
		final List<Project> projects = new ArrayList<>(BugrapFacade.getInstance().findProjects());
		Collections.sort(projects);
		return projects;
	}

	public List<ProjectVersion> listProjectVersions() {

		if (project == null) {
			return Collections.emptyList();
		}

		final List<ProjectVersion> projectVersions = new ArrayList<>(
				BugrapFacade.getInstance().findProjectVersions(project));
		Collections.sort(projectVersions);
		return projectVersions;
	}

	public List<Report> listReports() {
		final ReportsQuery query = new ReportsQuery();
		query.project = project;
		query.projectVersion = projectVersion;

		return new ArrayList<>(BugrapFacade.getInstance().findReports(query));
	}

	public boolean isAllVersionsSelected() {
		return projectVersion == null;
	}

	public String getElapsedTimeFrom(final Date timestamp) {

		final Instant timestampInstant = Instant.ofEpochMilli(timestamp.getTime());
		final Instant now = Instant.now();

		final Period period = Period.between(asLocalDate(timestampInstant), asLocalDate(now));

		if (period.get(ChronoUnit.YEARS) > ZERO) {
			return period.get(ChronoUnit.YEARS) + " years ago";
		}

		if (period.get(ChronoUnit.MONTHS) > ZERO) {
			return period.get(ChronoUnit.MONTHS) + " months ago";
		}

		if (period.get(ChronoUnit.DAYS) > ZERO) {
			final long days = period.get(ChronoUnit.DAYS);
			if (days > ONE_WEEK_IN_DAYS) {
				return period.get(ChronoUnit.WEEKS) + " weeks ago";
			}

			return days + "days ago";
		}

		final long seconds = Duration.between(timestampInstant, now).getSeconds();
		if (seconds > ONE_HOUR_IN_SECONDS) {
			return seconds / ONE_HOUR_IN_SECONDS + " hours ago";
		}

		if (seconds > ONE_MINUTE_IN_SECONDS) {
			return seconds / ONE_MINUTE_IN_SECONDS + " minutes ago";
		}

		if (seconds > ZERO) {
			return seconds + " seconds ago";
		}

		return " just now";
	}

	private LocalDate asLocalDate(final Instant instant) {
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public String getAssignedTo(final Reporter reporter) {
		if (reporter == null) {
			return null;
		}
		return reporter.getName();
	}

}
