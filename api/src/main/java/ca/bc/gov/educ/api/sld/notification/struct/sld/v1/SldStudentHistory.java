package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sld student history.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudentHistory {
  /**
   * The Dist no.
   */
  private String distNo;
  /**
   * The Schl no.
   */
  private String schlNo;
  /**
   * The Report date.
   */
  private long reportDate;
  /**
   * The Local student id.
   */
  private String localStudentId;
  /**
   * The Enrolled grade code.
   */
  private String enrolledGradeCode;
  /**
   * The Sex.
   */
  private String sex;
  /**
   * The Birth date.
   */
  private String birthDate;
  /**
   * The Legal surname.
   */
  private String legalSurname;
  /**
   * The Legal given name.
   */
  private String legalGivenName;
  /**
   * The Legal middle name.
   */
  private String legalMiddleName;
  /**
   * The Usual surname.
   */
  private String usualSurname;
  /**
   * The Usual given name.
   */
  private String usualGivenName;
  /**
   * The Usual middle name.
   */
  private String usualMiddleName;
  /**
   * The Pen.
   */
  private String pen;
  /**
   * The Source.
   */
  private String source;
  /**
   * The Postal.
   */
  private String postal;
}
