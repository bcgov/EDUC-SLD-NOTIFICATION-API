package ca.bc.gov.educ.api.sld.notification.health;

import io.nats.client.Connection;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * The type Sld notification api custom health check.
 */
@Component
public class SldNotificationAPICustomHealthCheck implements HealthIndicator {
  /**
   * The Nats connection.
   */
  private final Connection natsConnection;

  /**
   * Instantiates a new Sld notification api custom health check.
   *
   * @param natsConnection the nats connection
   */
  public SldNotificationAPICustomHealthCheck(final Connection natsConnection) {
    this.natsConnection = natsConnection;
  }

  @Override
  public Health getHealth(final boolean includeDetails) {
    return this.healthCheck();
  }


  @Override
  public Health health() {
    return this.healthCheck();
  }

  /**
   * Health check health.
   *
   * @return the health
   */
  private Health healthCheck() {
    if (this.natsConnection.getStatus() == Connection.Status.CLOSED) {
      return Health.down().withDetail("NATS", " Connection is Closed.").build();
    }
    return Health.up().build();
  }
}
