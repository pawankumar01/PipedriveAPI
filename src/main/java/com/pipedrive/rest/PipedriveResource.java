package com.pipedrive.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pipedrive.rest.pipedrive.api.PipedriveAPI;
import com.pipedrive.rest.util.Injection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *  Class represent REST API for Pipedrive related data.
 *  Base URL - http://<server_url>/api/v1/pd/
 */

@Path("pd")
public class PipedriveResource {
  private PipedriveAPI pdapi = Injection.providePipeDriveAPI();
  private Gson gson = Injection.provideGSONObject();


  /**
   * Fetches all the organization from the Pipedrive API.
   * @return
   */
  @GET
  @Path("/organizations")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllOrganizations() {
    String response = pdapi.getAllOrganizations(0);
    //System.out.println("Response body" + response);
    if(response == null)
      return buildErrorMessage();

    return Response.ok(response).build();
  }


  /**
   * Fetches all organizationFields from the Pipedrive API.
   * @return
   */
  @GET
  @Path("/organizationFields")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllOrganizationFields() {
    String response = pdapi.getAllOrganizationFields();
//    System.out.println("Response body" + response);
    if(response == null)
      return buildErrorMessage();
    return Response.ok(response).build();
  }


  /**
   * Fetch Organization associated with orgId from PipedriveAPI
   * @param orgId
   * @return
   */

  @GET
  @Path("/organizations/{orgId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrganizationById(@PathParam("orgId") String orgId) {
    String orgData = pdapi.getOrganizationById(orgId);
   // System.out.println("Response body" + orgData);
    if(orgData == null)
      return buildErrorMessage();

    return Response.ok(orgData).build();
  }


  /**
   * Update Organization with associated ID
   * @param orgId
   * @return
   */

  @PUT
  @Path("/organizations/{orgId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateOrganization(@PathParam("orgId") String orgId, String orgData) {
    String response = pdapi.updateOrganization(orgId, orgData);
//    System.out.println("Response body" + orgData);
    if(response == null)
      return buildErrorMessage();

    return Response.ok(response).build();
  }


  /**
   * Creates a new organization
   * @return
   */

  @POST
  @Path("/organizations/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrganization(String orgData) {
    String response = pdapi.createOrganization(orgData);
   // System.out.println("Response body" + orgData);
    if(response == null)
      return buildErrorMessage();

    return Response.ok(response).build();
  }



  /**
   * Fetches 3 nearest orgs with reference to user's latitude and longitude
   * @return
   */

  @GET
  @Path("/nearestorg/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response fetchNearestOrgs(@QueryParam("lat") Double mylat, @QueryParam("lng") Double mylng) {
    String response = pdapi.fetchNearestOrganization(mylat, mylng);
    //System.out.println("Response body" + response);
    return Response.ok(response).build();
  }

  /**
   * Delete Organization with specified orgId
   * @param orgId orgId to delete
   * @return
   */
  @DELETE
  @Path("/organizations/{orgId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrganization(@PathParam("orgId") String orgId) {
    String response = pdapi.deleteOrganization(orgId);
    return Response.ok(response).build();
  }

  private Response buildErrorMessage(){
    JsonObject obj = new JsonObject();
    obj.addProperty("desc", "Something is not right! Please try again after sometime");
    return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(obj.toString()).build();

  }
}
