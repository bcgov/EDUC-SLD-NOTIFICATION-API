package ca.bc.gov.educ.api.sld.notification.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Application properties.
 */
@Component
@Getter
@Setter
public class ApplicationProperties {
  public static final String CORRELATION_ID = "correlationID";
  /**
   * The constant API_NAME.
   */
  public static final String API_NAME = "SLD-NOTIFICATION-API";
  /**
   * The Nats url.
   */
  @Value("${nats.url}")
  String natsUrl;
  /**
   * The Nats max reconnect.
   */
  @Value("${nats.maxReconnect}")
  Integer natsMaxReconnect;

}
