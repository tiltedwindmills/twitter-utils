package org.tiltedwindmills.twitter.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Endpoints used to mimic the Twitter API in our simulated environment.
 *
 * @author John Daniel
 */
@RestController
public final class TwitterAPISimulator {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterAPISimulator.class);

    @Value("${data.simulator.enabled}")
    private boolean simulated;

    /**
     * Retrieves the simulated Twitter favorites and pushes them as JSON content to the requester.
     *
     * @return the metadata needed for Spring to construct a response
     * @throws IOException if a problem occurs reading the simulated data file.
     */
    @RequestMapping(value = "/simulator/favorites", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getFavorites() throws IOException {

        if (!simulated) {
            return new ResponseEntity<>("Simulated Data not available", HttpStatus.FORBIDDEN);
        }

        final Resource resource = new ClassPathResource("data/dirtiescontext_favorites_2015-09-18.json");
        return new ResponseEntity<>(IOUtils.toString(resource.getInputStream(), "UTF-8"), HttpStatus.OK);
    }
}