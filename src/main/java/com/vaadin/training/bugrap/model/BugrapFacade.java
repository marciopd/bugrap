package com.vaadin.training.bugrap.model;

import org.vaadin.bugrap.domain.BugrapRepository;

public class BugrapFacade extends BugrapRepository {

	private static final long serialVersionUID = -6620445191583179849L;

	private static final BugrapFacade INSTANCE = new BugrapFacade();
	private static final String DATABASE_PATH = "/tmp/bugrap;create=true";

	private BugrapFacade() {
		super(DATABASE_PATH);
	}

	public static BugrapFacade getInstance() {
		return INSTANCE;
	}
}
