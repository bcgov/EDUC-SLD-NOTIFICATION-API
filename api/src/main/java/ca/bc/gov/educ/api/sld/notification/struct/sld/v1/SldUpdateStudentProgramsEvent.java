package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sld update student programs event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldUpdateStudentProgramsEvent {
  /**
   * The Pen.
   */
  private String pen;
  /**
   * The Sld student program.
   */
  private SldStudentProgram sldStudentProgram;
}
