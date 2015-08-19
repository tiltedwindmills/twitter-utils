package org.tiltedwindmills.twitter.utils.service;

import org.springframework.social.twitter.api.Tweet;

import java.util.ArrayList;

/**
 * Crappy little workaround to turn a generic into a concrete so we can properly provide Type in Spring REST clients.
 * Stolen shamelessly from Spring examples.  Apologies to those authors for not writing down the details to provide
 * better credit, but its down in the Spring Social code somewhere.
 *
 * @author John Daniel via Spring source code.
 */
class TweetList extends ArrayList<Tweet> {

    // does nothing.  :(
}