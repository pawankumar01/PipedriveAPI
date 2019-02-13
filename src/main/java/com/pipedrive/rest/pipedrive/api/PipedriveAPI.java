package com.pipedrive.rest.pipedrive.api;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pipedrive.rest.util.BoundedPriorityQueue;
import com.pipedrive.rest.util.Injection;
import com.squareup.okhttp.*;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

/**
 *  Class communicates with the PipeDrive API
 */

public class PipedriveAPI {

    private static final String apiKey = Injection.getAPIKey();
    private static final String BASE_URL = "https://api.pipedrive.com/v1/";

    private static final String FIELDS = ":(id,name,owner_id,company_id,add_time,people_count," +
            "open_deals_count,closed_deals_count,address,address_formatted_address,cc_email,owner_name)";

    private static final String ORG_URL = BASE_URL + "organizations" ;//+ FIELDS;
    private static final String ORG_FIELDS_URL = BASE_URL + "organizationFields" ;//+ FIELDS;

    private static final int LIMIT = 500;
    private static final boolean ENABLE_LOGGING = false;
    private static final int MAX_CLOSEST_LIMIT = 3;

    private OkHttpClient client;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private Gson gson = Injection.provideGSONObject();


    public PipedriveAPI() {
        client = new OkHttpClient();

        if(ENABLE_LOGGING) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            client.interceptors().add(logging);
        }
    }

    public String getAllOrganizations(int start) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_URL + "?api_token=" + apiKey+"&limit=" +
                LIMIT+ "&start=" + start).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAllOrganizationFields() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_FIELDS_URL + "?api_token=" + apiKey).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String getOrganizationById(String orgId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_URL + "/" +orgId+"?api_token=" + apiKey).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String updateOrganization(String orgId, String orgData) {
        RequestBody body = RequestBody.create(JSON, orgData);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_URL + "/" +orgId+"?api_token=" + apiKey).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createOrganization(String orgData) {
        RequestBody body = RequestBody.create(JSON, orgData);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_URL +"?api_token=" + apiKey).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String deleteOrganization(String orgId) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(ORG_URL + "/" +orgId+"?api_token=" + apiKey).newBuilder();
        String url = urlBuilder.build().toString();

        //System.out.println("Json is" + JSON);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method fetches data from the PipeDrive and calculates distance between User's lat/lng.
     * Stores top 3 element
     * @param start
     * @param pq
     * @param mylat
     * @param myLng
     */

    private void fetchNearestOrganization(int start, BoundedPriorityQueue<JsonObject> pq, Double mylat, Double myLng){
        String s = getAllOrganizations(start);
        if(s == null) return;
      //  System.out.println("raw object"  + s);

        JsonObject convertedObject = gson.fromJson(s, JsonObject.class);
        JsonArray array = convertedObject.getAsJsonArray("data");
        Type listType = new TypeToken<List<JsonObject>>() {}.getType();
        List<JsonObject> yourList = new Gson().fromJson(array, listType);


        for(JsonObject o : yourList){
            String address = getNullAsEmptyString(o.get("address"));
            if(address != null){
                String[] split = address.split(",");

                /**
                 *  It should contains both latitude and longitude
                 *  else ignore this org
                 */
                if(split.length == 2){
                    Double lat = Double.parseDouble(split[0]);
                    Double lng = Double.parseDouble(split[1]);

                    /**
                     *  Checking if latitude and longitude fall in range,
                     *  If not ignore this organization
                     */
                    if(lat>= -90 && lat<=90 && lng>=-180 && lng<=180) {
                        Double distance = getDistanceBetweenTwoPoints(mylat, myLng, lat, lng);
                        o.addProperty("distance", distance);
                        pq.offer(o);
                    }
                }
            }
        }

        JsonObject additionalData = convertedObject.getAsJsonObject("additional_data");
        JsonObject pagination = additionalData.getAsJsonObject("pagination");
        Boolean b = pagination.get("more_items_in_collection").getAsBoolean();

        if(b == true){
            int next_start = pagination.get("next_start").getAsInt();
            /**
             *  Fetch if more item exists in the Pipedrive as limit is 500
             *  https://pipedrive.readme.io/docs/using-pagination-to-retrieve-all-deal-titles
             */
            fetchNearestOrganization(next_start, pq, mylat, myLng);
        }

    }
    private String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

    /***
     * Calculate distance between two points using latitude and longitude
     * http://www.movable-type.co.uk/scripts/latlong.html
     * Haversine Formula distance
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */

    public double getDistanceBetweenTwoPoints(double lat1, double lon1, double lat2, double lon2){

        double R = 6371e3; // metres
        double φ1 = Math.toRadians(lat1);
        double φ2 =  Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2-lat1);
        double Δλ = Math.toRadians(lon2-lon1);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;
        return Math.floor(d);
    }

    /**
     * Fetches Nearest Organization based on User's latitude and longitude.
     * @param mylat
     * @param myLng
     * @return
     */

    public String fetchNearestOrganization(Double mylat, Double myLng){
        BoundedPriorityQueue<JsonObject> pq = new BoundedPriorityQueue<>(new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject o1, JsonObject o2) {
                return (int)(o2.get("distance").getAsDouble() - o1.get("distance").getAsDouble());
            }
        }, MAX_CLOSEST_LIMIT);
        fetchNearestOrganization(0, pq, mylat, myLng);
        return gson.toJson(pq.toArray());
    }

    public static void main(String[] args) {
        Double mylat = 28.4756546;
        Double myLng = 77.0742033;
        String x = new PipedriveAPI().fetchNearestOrganization(mylat, myLng);
        System.out.println(x);
    }

}
