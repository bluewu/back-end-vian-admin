package com.vian.admin;

import com.vian.admin.config.VianAdminProperties;
import com.vian.microservice.autoconfigure.OAuth2InterceptedFeignConfiguration;
import com.vian.microservice.autoconfigure.VianMicroserviceProperties;
import com.vian.microservice.constants.MicroserviceConstants;
import com.vian.microservice.util.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@ComponentScan(
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = OAuth2InterceptedFeignConfiguration.class))
@EnableAutoConfiguration(
    exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({VianMicroserviceProperties.class, VianAdminProperties.class})
@EnableDiscoveryClient
public class VianAdminApp {

  private static final Logger log = LoggerFactory.getLogger(VianAdminApp.class);

  @Inject private Environment env;

  /**
   * Main method, used to run the application.
   *
   * @param args the command line arguments
   * @throws UnknownHostException if the local host name could not be resolved into an address
   */
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(VianAdminApp.class);
    DefaultProfileUtil.addDefaultProfile(app);
    Environment env = app.run(args).getEnvironment();
    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Application '{}' is running! Access URLs:\n\t"
            + "Local: \t\thttp://127.0.0.1:{}\n\t"
            + "External: \thttp://{}:{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        env.getProperty("server.port"),
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"));

    String configServerStatus = env.getProperty("configserver.status");
    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Config Server: \t{}\n----------------------------------------------------------",
        configServerStatus == null
            ? "Not found or not setup for this application"
            : configServerStatus);
  }

  /**
   * Initializes InsuranceProductApp.
   *
   * <p>Spring profiles can be configured with a program arguments
   * --spring.profiles.active=your-active-profile
   *
   * <p>You can find more information on how profiles work with JHipster on <a
   * href="http://jhipster.github.io/profiles/">http://jhipster.github.io/ profiles/</a>.
   */
  @PostConstruct
  public void initApplication() {
    log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(MicroserviceConstants.SPRING_PROFILE_DEVELOPMENT)
        && activeProfiles.contains(MicroserviceConstants.SPRING_PROFILE_PRODUCTION)) {
      log.error(
          "You have misconfigured your application! It should not run "
              + "with both the 'dev' and 'prod' profiles at the same time.");
    }
    if (activeProfiles.contains(MicroserviceConstants.SPRING_PROFILE_DEVELOPMENT)
        && activeProfiles.contains(MicroserviceConstants.SPRING_PROFILE_CLOUD)) {
      log.error(
          "You have misconfigured your application! It should not"
              + "run with both the 'dev' and 'cloud' profiles at the same time.");
    }
  }
}
