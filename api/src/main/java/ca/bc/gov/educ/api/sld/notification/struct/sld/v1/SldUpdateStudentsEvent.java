package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type sld update students event.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldUpdateStudentsEvent {
  /**
   * the PEN which is used to search the sld students.
   */
  private String pen;
  /**
   * the attributes of sld record to be updated.
   * Leave the attribute null if no update.
   */
  private SldStudent sldStudent;
}