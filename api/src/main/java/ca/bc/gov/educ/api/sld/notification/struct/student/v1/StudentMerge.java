package ca.bc.gov.educ.api.sld.notification.struct.student.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type Student merge.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentMerge implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = -8133441569621088814L;
  /**
   * The Student merge id.
   */
  String studentMergeID;
  /**
   * The Student id.
   */
  @NotNull(message = "Student ID can not be null.")
  String studentID;
  /**
   * The Merge student id.
   */
  @NotNull(message = "Merge Student ID can not be null.")
  String mergeStudentID;
  /**
   * The Student merge direction code.
   */
  @NotNull(message = "Student Merge Direction Code can not be null.")
  String studentMergeDirectionCode;
  /**
   * The Student merge source code.
   */
  @NotNull(message = "Student Merge Source Code can not be null.")
  String studentMergeSourceCode;
  /**
   * The Create user.
   */
  @Size(max = 100)
  String createUser;
  /**
   * The Update user.
   */
  @Size(max = 100)
  String updateUser;
}
