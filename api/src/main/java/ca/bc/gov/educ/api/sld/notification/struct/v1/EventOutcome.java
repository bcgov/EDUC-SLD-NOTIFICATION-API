package ca.bc.gov.educ.api.sld.notification.struct.v1;

/**
 * The enum Event outcome.
 */
public enum EventOutcome {
  /**
   * Possible match added event outcome.
   */
  POSSIBLE_MATCH_ADDED,
  /**
   * Possible match deleted event outcome.
   */
  POSSIBLE_MATCH_DELETED,
  /**
   * Student created event outcome.
   */
  STUDENT_CREATED,
  /**
   * Merge created event outcome.
   */
  MERGE_CREATED,
  /**
   * Student updated event outcome.
   */
  STUDENT_UPDATED,
  /**
   * Merge deleted event outcome.
   */
  MERGE_DELETED,
  /**
   * Student not found event outcome.
   */
  STUDENT_NOT_FOUND
}
