package ca.bc.gov.educ.api.sld.notification.exception;

import lombok.Getter;

/**
 * The enum Business error.
 */
public enum BusinessError {
  /**
   * The Event already persisted.
   */
  EVENT_ALREADY_PERSISTED("Event with event id :: $? , is already persisted in DB, a duplicate message from Jet Stream."),
  /**
   * The Student not found.
   */
  STUDENT_NOT_FOUND("Student not found, this should not have happened, will be retried."),
  /**
   * The Sld update failed.
   */
  SLD_UPDATE_FAILED("Update from SLD API failed, this should not have happened, will be retried.");
  @Getter
  private final String code;

  BusinessError(String code) {
    this.code = code;

  }
}
