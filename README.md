
Twitter Utilities
=========

A set of utility functions for dealing with Twitter data, primarily regarding import/export of data.  The planned
utilities are:

1. Favorites export.
2. List to Follows comparison, showing users who are listed but not followed and vice versa.


Additional goals of the project are to get familiar with:

* Twitter API
* Gradle build system
* IntelliJ IDE
* ( a little more ) Thymeleaf templating


Per usual, the project is bootstrapped with [Spring Boot](http://projects.spring.io/spring-boot/).


Project attempts to use:

* [Semantic versioning](http://semver.org/)


### Notes

* Tried to use Spring Social for Twitter integration, but its just not ready for search applications.  There's little
consistency in the methods and options for advanced searching aren't there.  ( For example.. cannot provide max_id to
the [_favorites/list_](https://dev.twitter.com/rest/reference/get/favorites/list) operation )