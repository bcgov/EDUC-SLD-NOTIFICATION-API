package ca.bc.gov.educ.api.sld.notification.struct.v1;

/**
 * The enum Event outcome.
 */
public enum EventOutcome {
  /**
   * The Possible match added.
   */
  POSSIBLE_MATCH_ADDED,
  /**
   * The Possible match deleted.
   */
  POSSIBLE_MATCH_DELETED,
  /**
   * The Student created.
   */
  STUDENT_CREATED,
  /**
   * The Merge created.
   */
  MERGE_CREATED,
  /**
   * The Student updated.
   */
  STUDENT_UPDATED,
  /**
   * The Merge deleted.
   */
  MERGE_DELETED,
  /**
   * The Student not found.
   */
  STUDENTS_NOT_FOUND,
  /**
   * Students found event outcome.
   */
  STUDENTS_FOUND
}
