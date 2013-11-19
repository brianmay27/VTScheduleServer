package edu.vt.ece4564.vtClassRequest.Date;

public class DateException extends Exception {

	private static final long serialVersionUID = 1283226263878636973L;

	private static final String MESSAGE = "Invalid day or time";

	public DateException() {
		super(MESSAGE);
	}
}
