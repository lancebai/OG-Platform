/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.config;

import com.opengamma.master.AbstractMaster;

/**
 * A general-purpose configuration master.
 * <p>
 * The configuration master provides a uniform view over storage of configuration elements.
 * This interface provides methods that allow the master to be searched and updated.
 * <p>
 * Many different kinds of configuration element may be stored using this interface.
 * Each element type will be stored using a different instance where the generic
 * parameter represents the type of the element.
 * 
 * @param <T>  the configuration element type
 */
public interface ConfigTypeMaster<T> extends AbstractMaster<ConfigDocument<T>> {

  /**
   * Searches for configuration documents matching the specified search criteria.
   * 
   * @param request  the search request, not null
   * @return the search result, not null
   * @throws IllegalArgumentException if the request is invalid
   */
  ConfigSearchResult<T> search(ConfigSearchRequest request);

  /**
   * Queries the history of a single piece of configuration.
   * <p>
   * The request must contain an object identifier to identify the configuration.
   * 
   * @param request  the history request, not null
   * @return the configuration history, not null
   * @throws IllegalArgumentException if the request is invalid
   */
  ConfigHistoryResult<T> history(ConfigHistoryRequest request);

}
