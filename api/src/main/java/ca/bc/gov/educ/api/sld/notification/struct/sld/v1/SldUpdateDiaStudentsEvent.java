package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sld update dia students event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldUpdateDiaStudentsEvent {
  /**
   * The Pen.
   */
  private String pen;
  /**
   * The Sld dia student.
   */
  private SldDiaStudent sldDiaStudent;
}
