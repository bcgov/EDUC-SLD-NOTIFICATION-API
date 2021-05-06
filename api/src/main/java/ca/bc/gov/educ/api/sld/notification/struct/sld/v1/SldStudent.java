package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sld student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldStudent {
  /**
   * The Student id.
   */
  private String studentId;
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
  private Long reportDate;
  /**
   * The Local student id.
   */
  private String localStudentId;
  /**
   * The Adult total crs hours.
   */
  private String adultTotalCrsHours;
  /**
   * The Enrolled grade code.
   */
  private String enrolledGradeCode;
  /**
   * The Student fte value.
   */
  private String studentFteValue;
  /**
   * The Student funded by code.
   */
  private String studentFundedByCode;
  /**
   * The Not elig for prov funding.
   */
  private String notEligForProvFunding;
  /**
   * The Native ancestry ind.
   */
  private String nativeAncestryInd;
  /**
   * The Qualified is student ind.
   */
  private String qualifiedIsStudentInd;
  /**
   * The Sex.
   */
  private String sex;
  /**
   * The Birth date.
   */
  private String birthDate;
  /**
   * The Home language spoken.
   */
  private String homeLanguageSpoken;
  /**
   * The Province code.
   */
  private String provinceCode;
  /**
   * The Country code.
   */
  private String countryCode;
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
   * The Band code.
   */
  private String bandCode;
  /**
   * The School funding code.
   */
  private String schoolFundingCode;
  /**
   * The Number of courses.
   */
  private String numberOfCourses;
  /**
   * The Birth month.
   */
  private String birthMonth;
  /**
   * The Birth day.
   */
  private String birthDay;
  /**
   * The College courses.
   */
  private String collegeCourses;
  /**
   * The Other courses.
   */
  private String otherCourses;
  /**
   * The Postal.
   */
  private String postal;
  /**
   * The Sped cat.
   */
  private String spedCat;
  /**
   * The Sped desc 2.
   */
  private String spedDesc2;
  /**
   * The Adult grad.
   */
  private String adultGrad;
  /**
   * The Fy adjust.
   */
  private String fyAdjust;
  /**
   * The Esl yrs.
   */
  private String eslYrs;
  /**
   * The Fund feb sped.
   */
  private String fundFebSped;
  /**
   * The Number of support blocks.
   */
  private String numberOfSupportBlocks;
  /**
   * The Grad.
   */
  private String grad;
}
