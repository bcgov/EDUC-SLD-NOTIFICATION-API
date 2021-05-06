package ca.bc.gov.educ.api.sld.notification.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The type Event entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SLD_NOTIFICATION_EVENT")
@Data
@DynamicUpdate
public class EventEntity {
  /**
   * The Create user.
   */
  @Column(name = "CREATE_USER", updatable = false)
  String createUser;
  /**
   * The Create date.
   */
  @Column(name = "CREATE_DATE", updatable = false)
  @PastOrPresent
  LocalDateTime createDate;
  /**
   * The Update user.
   */
  @Column(name = "UPDATE_USER")
  String updateUser;
  /**
   * The Update date.
   */
  @Column(name = "UPDATE_DATE")
  @PastOrPresent
  LocalDateTime updateDate;
  /**
   * The Sld notification event id.
   */
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "SLD_NOTIFICATION_EVENT_EVENT_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID sldNotificationEventId;
  /**
   * The Event id.
   */
  @Column(name = "EVENT_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID eventId;
  /**
   * The Event payload bytes.
   */
  @NotNull(message = "eventPayload cannot be null")
  @Lob
  @Column(name = "EVENT_PAYLOAD")
  @ToString.Exclude
  private byte[] eventPayloadBytes;
  /**
   * The Event status.
   */
  @NotNull(message = "eventStatus cannot be null")
  @Column(name = "EVENT_STATUS")
  private String eventStatus;
  /**
   * The Event type.
   */
  @NotNull(message = "eventType cannot be null")
  @Column(name = "EVENT_TYPE")
  private String eventType;
  /**
   * The Event outcome.
   */
  @NotNull(message = "eventOutcome cannot be null.")
  @Column(name = "EVENT_OUTCOME")
  private String eventOutcome;

  /**
   * The Event payload.
   */
  @Transient
  private String eventPayload; // for toString purpose only, easier to read and debug than byte[]

  /**
   * Gets event payload.
   *
   * @return the event payload
   */
  public String getEventPayload() {
    return new String(this.getEventPayloadBytes(), StandardCharsets.UTF_8);
  }

  /**
   * Sets event payload.
   *
   * @param eventPayload the event payload
   */
  public void setEventPayload(final String eventPayload) {
    this.setEventPayloadBytes(eventPayload.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * The type Event builder.
   */
  public static class EventEntityBuilder {
    /**
     * The Event payload bytes.
     */
    byte[] eventPayloadBytes;
    /**
     * The Event payload.
     */
    String eventPayload;

    /**
     * Event payload event builder.
     *
     * @param eventPayload the event payload
     * @return the event builder
     */
    public EventEntity.EventEntityBuilder eventPayload(final String eventPayload) {
      this.eventPayload = eventPayload;
      this.eventPayloadBytes = eventPayload.getBytes(StandardCharsets.UTF_8);
      return this;
    }
  }
}
