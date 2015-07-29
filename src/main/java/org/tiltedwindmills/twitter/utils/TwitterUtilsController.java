package org.tiltedwindmills.twitter.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


@Controller
@PropertySource("classpath:twitter.properties")
public final class TwitterUtilsController {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterUtilsController.class);


    private static final String BASE_URL = "https://api.twitter.com/1.1/favorites/list.json";
    private static final int PAGE_SIZE = 200;

    @Value("${foo.value}")
    private String foo;


    @Value("${spring.social.twitter.appId}")
    private String apiKey;

    @PostConstruct
    private void postConstruct() {

        checkNotNull(foo, "foo cannot be null");
        checkNotNull(apiKey, "apiKey cannot be null");
    }

    @RequestMapping("/test")
    public String list(final Map<String, Object> model) {

        model.put("message", "Foo: " + foo + " ... API Key : " + apiKey);
        return "index";
    }

    @Inject
    private Twitter twitter;

    @Inject
    private ConnectionRepository connectionRepository;

//    @Inject
//    public TwitterUtilsController(Twitter twitter, ConnectionRepository connectionRepository) {
//        this.twitter = twitter;
//        this.connectionRepository = connectionRepository;
//    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloTwitter(Model model) {

        LOG.debug("in home method");

        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitter.userOperations().getUserProfile());

        final List<Tweet> allFavorites = new ArrayList<>();

        List<Tweet> someFavorites = getFavorites();

        while (!CollectionUtils.isEmpty(someFavorites)) {

            LOG.debug("Adding {} favorites", someFavorites.size());

            long max_id = Long.MAX_VALUE;

            for (Tweet tweet : someFavorites) {
                if (tweet != null) {

                    // see if we found an older favorite.
                    if (tweet.getId() < max_id) {
                        max_id = tweet.getId();
                    }

                    allFavorites.add(tweet);
                }
            }

            // get the next batch;
            someFavorites = getFavorites(String.valueOf(max_id - 1));
        }

        LOG.info("Retrieval complete.  Found {} favorites", allFavorites.size());
        model.addAttribute("favorites", allFavorites);
        return "hello";
    }


    private List<Tweet> getFavorites() {
        return getFavorites(null);
    }

    private List<Tweet> getFavorites(final String maxId) {

        LOG.debug("Searching for favorites with max_id '{}'", maxId);

        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL);
        uriComponentsBuilder.queryParam("count", PAGE_SIZE);

        // add the max_id param if appropriate
        if (StringUtils.isNotBlank(maxId)) {
            uriComponentsBuilder.queryParam("max_id", maxId);
        }

        final URI target = uriComponentsBuilder.build().toUri();
        LOG.debug("Requesting favorites from {}", target);
        return twitter.restOperations().getForObject(target, TweetList.class);
    }


    private static class TweetList extends ArrayList<Tweet> {}
}
