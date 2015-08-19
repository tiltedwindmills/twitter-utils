package org.tiltedwindmills.twitter.utils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Simulated service implementation which provides canned responses to Twitter operations.  This is provided to ease
 * development by returning controlled data for reliable testing, and to reduce the likelihood of hitting the Twitter
 * API limit during development sessions.
 *
 * @see org.tiltedwindmills.twitter.utils.service.TwitterService
 * @see org.tiltedwindmills.twitter.utils.service.TwitterServiceImpl
 * @author John Daniel
 */
@Component
public final class SimulatedTwitterServiceImpl implements TwitterService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulatedTwitterServiceImpl.class);

    @Value("${data.simulator.enabled}")
    private boolean simulated;

    @Value("${data.simulator.url}")
    private String simulatedUrl;

    @Inject
    private Twitter twitter;

    @PostConstruct
    private void postConstruct() {

        if (simulated) {
            checkNotNull(simulatedUrl, "simulated Url cannot be null if simulation mode is enabled");
        }
    }

    @Override
    public TwitterProfile getUserProfile(final String screenName) {
        return new TwitterProfile(1L,"testScreenName", "James Bond", "url", null, null, null, null);
    }

    @Override
    public List<Tweet> getFavorites(final String screenName) {

        LOG.debug("Requesting simulated favorites from {}", simulatedUrl);
        return twitter.restOperations().getForObject(simulatedUrl, TweetList.class);
    }
}
