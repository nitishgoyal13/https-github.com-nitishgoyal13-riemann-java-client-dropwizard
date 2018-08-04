package io.riemann.dropwizard;

import com.codahale.metrics.riemann.Riemann;
import com.codahale.metrics.riemann.RiemannReporter;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


/**
 * /***
 * Created by nitish.goyal on 04/08/18
 ***/
@Slf4j
public abstract class RiemannDropwizardBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private static Riemann riemann;

    private static RiemannReporter riemannReporter;

    public abstract RiemannDropwizardConfig getRiemannConfiguration(T configuration);

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) {
        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                if (riemann == null) {
                    final val riemannConfig = getRiemannConfiguration(configuration);
                    if (riemannConfig == null || Strings.isNullOrEmpty(riemannConfig.getHost())) {
                        log.warn("No valid reimann host found!. Cannot start reimann bundle");
                        return;
                    }
                    try {
                        String host = System.getenv("HOST");
                        if (host == null) {
                            host = InetAddress.getLocalHost().getHostName();
                        }
                        riemann = new Riemann(riemannConfig.getHost(), riemannConfig.getPort());
                        RiemannReporter.Builder builder = RiemannReporter.forRegistry(environment.metrics())
                                .tags(riemannConfig.getTags())
                                .prefixedWith(riemannConfig.getPrefix())
                                .useSeparator(".")
                                .localHost(host)
                                .convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS);
                        riemannReporter = builder.build(riemann);
                        riemannReporter.start(riemannConfig.getPollingInterval(), TimeUnit.SECONDS);
                        log.info("Started Riemann metrics reporter on {}:{} with prefix {} and tagged with: {}", riemannConfig.getHost(),
                                riemannConfig.getPort(), riemannConfig.getPrefix(), Joiner.on(",").join(riemannConfig.getTags()));
                    } catch (IOException e) {
                        log.error("Error starting Riemann reporter: ", e.getMessage());
                    }
                }
            }

            @Override
            public void stop() throws Exception {
                if (riemannReporter != null) {
                    riemannReporter.stop();
                }
                if (riemann != null) {
                    riemann.close();
                }
            }
        });
    }
}

