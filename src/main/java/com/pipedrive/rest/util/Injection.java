package com.pipedrive.rest.util;

import com.google.gson.Gson;
import com.pipedrive.rest.pipedrive.api.PipedriveAPI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A simple dependence injector which creates and provides app's objects.
 */
public class Injection {

  private static Gson gson = new Gson();
  private static final String PROPERTIES_FILENAME = "pipedrive.properties";
  private static String apiKey = null;
  private static PipedriveAPI pdapi = new PipedriveAPI();

  public static PipedriveAPI providePipeDriveAPI(){
        return pdapi;
  }

  public static Gson provideGSONObject(){
    return gson;
  }

  public static String getAPIKey(){

      if(apiKey != null) return apiKey;

      Properties properties = new Properties();
      try {
        InputStream in = Injection.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
        properties.load(in);

      } catch (IOException e) {
        System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                + " : " + e.getMessage());
        System.exit(1);
      }
      apiKey = properties.getProperty("pipedrive.apikey");
      return apiKey;
  }


}
