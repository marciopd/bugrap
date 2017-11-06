package com.vaadin.training.bugrap.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ElapsedTimeFormat {

	private static final String LABEL_SECONDS_AGO = " seconds ago";
	private static final String LABEL_MINUTES_AGO = " minutes ago";
	private static final String LABEL_HOURS_AGO = " hours ago";
	private static final String LABEL_DAYS_AGO = "days ago";
	private static final String LABEL_WEEKS_AGO = " weeks ago";
	private static final String LABEL_MONTHS_AGO = " months ago";
	private static final String LABEL_YEARS_AGO = " years ago";
	private static final int ONE_MINUTE_IN_SECONDS = 60;
	private static final int ONE_HOUR_IN_SECONDS = 3600;
	private static final int ONE_WEEK_IN_DAYS = 7;
	private static final int ZERO = 0;
	private static final String LABEL_JUST_NOW = " just now";

	public static String format(final Date timestamp) {

	System.out.println(timestamp);

		final Instant timestampInstant = Instant.ofEpochMilli(timestamp.getTime());
		final Instant now = Instant.now();

		final Period period = Period.between(asLocalDate(timestampInstant), asLocalDate(now));

		if (period.get(ChronoUnit.YEARS) > ZERO) {
			return period.get(ChronoUnit.YEARS) + LABEL_YEARS_AGO;
		}

		if (period.get(ChronoUnit.MONTHS) > ZERO) {
			return period.get(ChronoUnit.MONTHS) + LABEL_MONTHS_AGO;
		}

		if (period.get(ChronoUnit.DAYS) > ZERO) {
			final long days = period.get(ChronoUnit.DAYS);
			if (days > ONE_WEEK_IN_DAYS) {
				return period.get(ChronoUnit.WEEKS) + LABEL_WEEKS_AGO;
			}

			return days + LABEL_DAYS_AGO;
		}

		final long seconds = Duration.between(timestampInstant, now).getSeconds();
		if (seconds > ONE_HOUR_IN_SECONDS) {
			return seconds / ONE_HOUR_IN_SECONDS + LABEL_HOURS_AGO;
		}

		if (seconds > ONE_MINUTE_IN_SECONDS) {
			return seconds / ONE_MINUTE_IN_SECONDS + LABEL_MINUTES_AGO;
		}

		if (seconds > ZERO) {
			return seconds + LABEL_SECONDS_AGO;
		}

		return LABEL_JUST_NOW;
	}

	private static LocalDate asLocalDate(final Instant instant) {
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
