package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

/**
 * The type Sld student program.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudentProgram {
  /**
   * The Student id.
   */
  @Size(max = 10)
  private String studentId;

  /**
   * The Dist no.
   */
  @Size(max = 3)
  private String distNo;

  /**
   * The Schl no.
   */
  @Size(max = 5)
  private String schlNo;

  /**
   * The Report date.
   */
  private Long reportDate;

  /**
   * The Enrolled program code.
   */
  @Size(max = 2)
  private String enrolledProgramCode;

  /**
   * The Career program.
   */
  @Size(max = 2)
  private String careerProgram;

  /**
   * The Pen.
   */
  @Size(max = 10)
  private String pen;

}
