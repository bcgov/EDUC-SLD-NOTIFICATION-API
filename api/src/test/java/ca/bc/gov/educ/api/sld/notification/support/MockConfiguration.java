package ca.bc.gov.educ.api.sld.notification.support;

import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.messaging.NatsConnection;
import ca.bc.gov.educ.api.sld.notification.messaging.jetstream.Subscriber;
import io.nats.client.Connection;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * The type Mock configuration.
 */
@Profile("test")
@Configuration
public class MockConfiguration {

  /**
   * Message publisher message publisher.
   *
   * @return the message publisher
   */
  @Bean
  @Primary
  public MessagePublisher messagePublisher() {
    return Mockito.mock(MessagePublisher.class);
  }

  @Bean
  @Primary
  public Connection connection() {
    return Mockito.mock(Connection.class);
  }

  @Bean
  @Primary
  public NatsConnection natsConnection() {
    return Mockito.mock(NatsConnection.class);
  }

  @Bean
  @Primary
  public Subscriber subscriber() {
    return Mockito.mock(Subscriber.class);
  }

}
