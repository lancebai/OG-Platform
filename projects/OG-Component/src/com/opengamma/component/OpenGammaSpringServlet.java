/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.util.rest.HttpMethodFilter;
import com.opengamma.util.rest.NoCachingFilter;
import com.opengamma.util.rest.UrlSuffixFilter;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.WebConfig;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * The OpenGamma servlet that integrates Jetty, Spring and OpenGamma components.
 */
public class OpenGammaSpringServlet extends SpringServlet {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;
  /** Logger. */
  private static final Logger s_logger = LoggerFactory.getLogger(ComponentManager.class);

  public OpenGammaSpringServlet() {
    super();
  }

  @Override
  public void init() throws ServletException {
    super.init();
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  @Override
  protected ResourceConfig getDefaultResourceConfig(Map<String, Object> props, WebConfig webConfig) throws ServletException {
    DefaultResourceConfig cfg = new DefaultResourceConfig();
    if (props.containsKey(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS) == false) {
      props.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, new ArrayList<Object>(Arrays.asList(new HttpMethodFilter(), new UrlSuffixFilter())));
    }
    if (props.containsKey(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS) == false) {
      props.put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, new ArrayList<Object>(Arrays.asList(new NoCachingFilter())));
    }
    cfg.setPropertiesAndFeatures(props);
    return cfg;
  }

  @Override
  protected void initiate(ResourceConfig rc, WebApplication wa) {
    ComponentRepository repo = ComponentRepository.getThreadLocal();
    final Set<Object> published = repo.getPublished().getAllSingletons();
    Application app = new Application() {
      @Override
      public Set<Object> getSingletons() {
        return published;
      }
    };
    
    try {
      // initialize the Jetty system
      rc.add(app);
      wa.initiate(rc, new SpringComponentProviderFactory(rc, getContext()));
      
    } catch (RuntimeException ex) {
      s_logger.error("Exception occurred during intialization", ex);
      throw ex;
    }
  }

}
