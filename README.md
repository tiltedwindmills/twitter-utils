
Twitter Utilities
=========

A set of utility functions for dealing with Twitter data, primarily regarding import/export of data.  The planned
utilities are:

1. Favorites export.
2. List to Follows comparison, showing users who are listed but not followed and vice versa.


Additional goals of the project are to get familiar with:

* Gradle build system
* IntelliJ IDE
* Twitter API
* ( a little more ) Thymeleaf templating


Per usual, the project is bootstrapped with [Spring Boot](http://projects.spring.io/spring-boot/).


Project attempts to use:

* [Semantic versioning](http://semver.org/)


### Notes

* App uses Spring Social for Twitter integration, though begrudgingly.  
  * The conventional **timelineOperations** methods don't seem ready for prime time.  There's little consistency in the
  methods and options for advanced searching aren't there.  ( For example.. cannot provide max_id to the 
  [_favorites/list_](https://dev.twitter.com/rest/reference/get/favorites/list) operation ).  
  
  * So, had to revert to the more generic **restOperations** methods and integrate that way.  Not perfect.

* For testing, there's a simulation mode that is, quite frankly, a bit hackish.  I've actually provided a URL endpoint
that simulates the Twitter API.  Preferred options were:

  1. Hard coded data, which was simply too much work given the complexity of the Twitter data.
  2. Data read from File and mapped via the same Jackson process as Spring Social uses.  I explored this one for a bit
  but given the lack of Jackson annotations on the Spring objects there was a need to use the
  [Jackson Mixin feature](http://wiki.fasterxml.com/JacksonMixInAnnotations), and I didn't feel like doing all that.

* Integrated [DarkAdmin](http://www.prepbootstrap.com/bootstrap-theme/dark-admin) theme.