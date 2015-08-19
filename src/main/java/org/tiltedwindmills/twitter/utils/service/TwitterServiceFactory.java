package org.tiltedwindmills.twitter.utils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory for obtaining a {@code TwitterService}.
 *
 * @author John Daniel
 */
@Named
public class TwitterServiceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterServiceFactory.class);

    @Value("${data.simulator.enabled}")
    private boolean simulated;

    @Inject
    private TwitterServiceImpl liveService;

    @Inject
    private SimulatedTwitterServiceImpl simulatedService;

    /** Validates the Spring construction */
    @PostConstruct
    private void postConstruct() {

        checkNotNull(liveService, "liveService cannot be null");
        checkNotNull(simulatedService, "simulatedService cannot be null");

        LOG.debug("Completed construction of TwitterServiceFactory");
        if (simulated) {
            LOG.info("!!! Data simulation is enabled. !!!");
        }
    }

    /**
     * Retrieves the appropriate {@code TwitterService} implementation based on the environment.
     * @return either a simulated or live {@code TwitterService}.
     */
    public final TwitterService getInstance() {

        if (simulated) {
            LOG.debug("Returning simulated twitter connection.");
            return simulatedService;
        }

        LOG.debug("Returning live twitter connection.");
        return liveService;
    }
}