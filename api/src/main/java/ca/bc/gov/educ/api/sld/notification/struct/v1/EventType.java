package ca.bc.gov.educ.api.sld.notification.struct.v1;

/**
 * The enum Event type.
 */
public enum EventType {
  /**
   * Create student event type.
   */
  CREATE_STUDENT,
  /**
   * Update student event type.
   */
  UPDATE_STUDENT,
  /**
   * Add possible match event type.
   */
  ADD_POSSIBLE_MATCH,
  /**
   * Delete possible match event type.
   */
  DELETE_POSSIBLE_MATCH,
  /**
   * Create merge event type.
   */
  CREATE_MERGE,

  /**
   * Delete Merge Data.
   */
  DELETE_MERGE,
  /**
   * Get students event type.
   */
  GET_STUDENTS,
  /**
   * Update sld students event type.
   */
  UPDATE_SLD_STUDENTS,
  /**
   * Update sld dia students event type.
   */
  UPDATE_SLD_DIA_STUDENTS,
  /**
   * Update sld student programs event type.
   */
  UPDATE_SLD_STUDENT_PROGRAMS,
}
