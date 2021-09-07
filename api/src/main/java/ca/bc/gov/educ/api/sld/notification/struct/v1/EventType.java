package ca.bc.gov.educ.api.sld.notification.struct.v1;

/**
 * The enum Event type.
 */
public enum EventType {
  /**
   * The Create student.
   */
  CREATE_STUDENT,
  /**
   * The Update student.
   */
  UPDATE_STUDENT,
  /**
   * The Add possible match.
   */
  ADD_POSSIBLE_MATCH,
  /**
   * The Delete possible match.
   */
  DELETE_POSSIBLE_MATCH,
  /**
   * The Create merge.
   */
  CREATE_MERGE,

  /**
   * The Delete merge.
   */
  DELETE_MERGE,
  /**
   * The Get students.
   */
  GET_STUDENTS,
  /**
   * The Update sld students.
   */
  UPDATE_SLD_STUDENTS,
  /**
   * The Update sld dia students.
   */
  UPDATE_SLD_DIA_STUDENTS,
  /**
   * The Update sld student programs.
   */
  UPDATE_SLD_STUDENT_PROGRAMS,
  /**
   * The Restore sld students.
   */
  RESTORE_SLD_STUDENTS,
  /**
   * The Restore sld dia students.
   */
  RESTORE_SLD_DIA_STUDENTS,
  /**
   * The Restore sld student programs.
   */
  RESTORE_SLD_STUDENT_PROGRAMS,
}
