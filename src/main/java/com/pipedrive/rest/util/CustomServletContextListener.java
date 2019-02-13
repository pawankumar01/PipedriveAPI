package com.pipedrive.rest.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listens to servlet callbacks
 */
public class CustomServletContextListener implements ServletContextListener {

  @Override public void contextInitialized(ServletContextEvent sce) {
    // Connect to database and create table for the first time
  }

  @Override public void contextDestroyed(ServletContextEvent sce) {

  }
}
