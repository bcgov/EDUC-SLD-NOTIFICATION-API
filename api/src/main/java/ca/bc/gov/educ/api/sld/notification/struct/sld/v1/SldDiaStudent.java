package ca.bc.gov.educ.api.sld.notification.struct.sld.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * The type Sld dia student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SldDiaStudent {

  /**
   * The Report date.
   */
  private Long reportDate;

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
   * The Record number.
   */
  private Long recordNumber;

  /**
   * The Pen.
   */
  @Size(max = 10)
  private String pen;

  /**
   * The Stud surname.
   */
  @Size(max = 25)
  private String studSurname;

  /**
   * The Stud given.
   */
  @Size(max = 25)
  private String studGiven;

  /**
   * The Stud middle.
   */
  @Size(max = 25)
  private String studMiddle;

  /**
   * The Usual surname.
   */
  @Size(max = 25)
  private String usualSurname;

  /**
   * The Usual given.
   */
  @Size(max = 25)
  private String usualGiven;

  /**
   * The Stud birth.
   */
  @Size(max = 8)
  private String studBirth;

  /**
   * The Stud sex.
   */
  @Size(max = 1)
  private String studSex;

  /**
   * The Stud grade.
   */
  @Size(max = 2)
  private String studGrade;

  /**
   * The Fte val.
   */
  private Long fteVal;

  /**
   * The Agreement type.
   */
  @Size(max = 1)
  private String agreementType;

  /**
   * The School name.
   */
  @Size(max = 40)
  private String schoolName;

  /**
   * The Schboard.
   */
  @Size(max = 3)
  private String schboard;

  /**
   * The Schnum.
   */
  @Size(max = 5)
  private String schnum;

  /**
   * The Schtype.
   */
  @Size(max = 20)
  private String schtype;

  /**
   * The Bandname.
   */
  @Size(max = 20)
  private String bandname;

  /**
   * The Frbandnum.
   */
  @Size(max = 20)
  private String frbandnum;

  /**
   * The Bandresnum.
   */
  @Size(max = 10)
  private String bandresnum;

  /**
   * The Band code.
   */
  @Size(max = 5)
  private String bandCode;

  /**
   * The Comment.
   */
  @Size(max = 70)
  private String comment;

  /**
   * The Pen status.
   */
  @Size(max = 2)
  private String penStatus;

  /**
   * The Orig pen.
   */
  @Size(max = 10)
  private String origPen;

  /**
   * The Siteno.
   */
  @Size(max = 1)
  private String siteno;

  /**
   * The Withdrawal code.
   */
  @Size(max = 2)
  private String withdrawalCode;

  /**
   * The Dia school info wrong.
   */
  @Size(max = 1)
  private String diaSchoolInfoWrong;

  /**
   * The Distno new.
   */
  @Size(max = 3)
  private String distnoNew;

  /**
   * The Schlno new.
   */
  @Size(max = 5)
  private String schlnoNew;

  /**
   * The Siteno new.
   */
  @Size(max = 1)
  private String sitenoNew;

  /**
   * The Stud new flag.
   */
  @Size(max = 1)
  private String studNewFlag;

  /**
   * The Pen comment.
   */
  @Size(max = 30)
  private String penComment;

  /**
   * The Posted pen.
   */
  @Size(max = 10)
  private String postedPen;
}
