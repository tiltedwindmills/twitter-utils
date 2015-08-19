package org.tiltedwindmills.twitter.utils.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Twitter accessor implementation for integrating with the live Twitter service.
 *
 * @see org.tiltedwindmills.twitter.utils.service.TwitterService
 * @see org.tiltedwindmills.twitter.utils.service.SimulatedTwitterServiceImpl
 * @author John Daniel
 */
@Component
public final class TwitterServiceImpl implements TwitterService {

    /** looooggggggeeeeerrrrrr */
    private static final Logger LOG = LoggerFactory.getLogger(TwitterServiceImpl.class);

    /** Twitter API endpoint for accessing favorite tweets */
    private static final String FAVORITES_BASE_URL = "https://api.twitter.com/1.1/favorites/list.json";

    /** Page size to use when retrieving tweets.  Should equal Twitter's maximum response. */
    private static final int PAGE_SIZE = 200;

    /** Spring Social interface for Twitter integration */
    @Inject
    private Twitter twitter;

    /** Validations and other post-initialization activities. */
    @PostConstruct
    private void postConstruct() {
        checkNotNull(twitter, "twitter interface cannot be null");
    }

    @Override
    public TwitterProfile getUserProfile(final String screenName) {
        return twitter.userOperations().getUserProfile(screenName);
    }

    @Override
    public List<Tweet> getFavorites(final String screenName) {

        //twitter.userOperations().getUserProfile();

        // construct the bucket for keeping the full list of favorites.
        final List<Tweet> allFavorites = new ArrayList<>();

        // get the first group of favorites, then loop through until we've exhausted the full list.
        List<Tweet> someFavorites = getFavoritesFromTwitter(screenName, null);
        while (!CollectionUtils.isEmpty(someFavorites)) {

            LOG.debug("Adding {} favorites", someFavorites.size());
            long maxId = Long.MAX_VALUE;

            for (Tweet tweet : someFavorites) {
                if (tweet != null) {

                    // see if we found an older favorite, and reset our maxId param if so.
                    if (tweet.getId() < maxId) {
                        maxId = tweet.getId();
                    }

                    // add this to our primary list.
                    allFavorites.add(tweet);
                }
            }

            // get the next batch;
            someFavorites = getFavoritesFromTwitter(screenName, String.valueOf(maxId - 1));
        }

        return allFavorites;
    }

    /** Retrieve {@code PAGE_SIZE} favorites older than ( or equal to ) the specified ID ) */
    private List<Tweet> getFavoritesFromTwitter(final String screenName, final String maxId) {

        LOG.debug("Searching for favorites with max_id '{}'", maxId);

        // build the base URL using known-needed parameters
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(FAVORITES_BASE_URL);
        uriComponentsBuilder.queryParam("count", PAGE_SIZE);
        uriComponentsBuilder.queryParam("screen_name", screenName);

        // add the max_id param if appropriate
        if (StringUtils.isNotBlank(maxId)) {
            uriComponentsBuilder.queryParam("max_id", maxId);
        }

        final URI target = uriComponentsBuilder.build().toUri();
        LOG.debug("Requesting favorites from {}", target);
        return twitter.restOperations().getForObject(target, TweetList.class);
    }
}
