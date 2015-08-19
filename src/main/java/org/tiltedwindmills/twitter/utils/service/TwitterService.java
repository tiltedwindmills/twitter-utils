package org.tiltedwindmills.twitter.utils.service;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.List;

/**
 * Specifies the operations for interacting with Twitter.
 *
 * @author John Daniel
 */
public interface TwitterService {

    /**
     * Retrieves a specific user's Twitter profile details.
     *
     * @param screenName the screen name for the user whose details are to be retrieved.
     * @return a {@link TwitterProfile} object representing the user's profile.
     */
    TwitterProfile getUserProfile(String screenName);

    /**
     * Retrieves the most recent tweets favorited by the specified user.
     *
     * @param screenName The screen name of the user whose favorites are being requested.
     * @return a collection of favorited {@code Tweet}s from the provided user's timeline.
     */
    List<Tweet> getFavorites(String screenName);
}