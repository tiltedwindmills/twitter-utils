package org.tiltedwindmills.twitter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tiltedwindmills.twitter.utils.service.TwitterService;
import org.tiltedwindmills.twitter.utils.service.TwitterServiceFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Primary application controller.
 *
 * @author John Daniel
 */
@Controller
@PropertySource(value = "classpath:twitter.properties", ignoreResourceNotFound = true)
public final class TwitterUtilsController {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterUtilsController.class);

    @Value("${data.simulator.enabled}")
    private boolean simulated;

    @Value("${twitter.screenname}")
    private String screenName;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    private TwitterServiceFactory twitterServiceFactory;

    private TwitterService twitterService;

    @PostConstruct
    private void postConstruct() {

        checkNotNull(twitterServiceFactory, "twitterServiceFactory cannot be null");
        checkNotNull(connectionRepository, "connectionRepository cannot be null");
        checkNotNull(screenName, "screenName cannot be null");

        // load the twitter service
        twitterService = twitterServiceFactory.getInstance();
        checkNotNull(twitterService, "twitterService cannot be null");

        // TODO : validate the Twitter Oauth info was provided if twitter.properties file was not present.
    }

    /**
     * Retrieves Twitter favorites from backend service and sends them to UI for display.
     *
     * @param model Spring M
     * @return The view name to use when displaying the retrieved data.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getFavorites(final Model model) {

        LOG.debug("in home method");

        // if we're not using simulated data and there's no existing connection, need to get a new one.
        if (!simulated && connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            // ConnectController defers this to view connect/twitterConnect
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitterService.getUserProfile(screenName));
        final List<Tweet> allFavorites = twitterService.getFavorites(screenName);

        LOG.info("Retrieval complete.  Found {} favorites", allFavorites.size());
        model.addAttribute("favorites", allFavorites);
        return "hello";
    }
}
