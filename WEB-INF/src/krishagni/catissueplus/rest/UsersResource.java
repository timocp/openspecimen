package krishagni.catissueplus.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dto.UserDetails;
import krishagni.catissueplus.handler.SpecimenHandler;
import krishagni.catissueplus.handler.UserHandler;

import com.google.gson.Gson;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

@Path("/old-users")
public class UsersResource {

	@Context
  private HttpServletRequest httpServletRequest;

  private static final Logger LOGGER = Logger.getCommonLogger(SpecimenResource.class);
  
  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
  public Response create(String userDetails)
  {
      Response response = null;
      try
      {
          Gson gson = AppUtility.initGSONBuilder().create();
          UserDetails details = gson.fromJson(userDetails, UserDetails.class);
          UserHandler userHandler = new UserHandler();
          details = userHandler.createUser(details, getSessionDataBean());
          response = Response.status(Status.CREATED.getStatusCode()).entity(details)
                  .type(MediaType.APPLICATION_JSON).build();
      }
      catch (CatissueException e)
      {
          LOGGER.error(e);
          response = getResponse(e);
      }
      catch (Exception e)
      {
          LOGGER.error(e);
          response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                  .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
      }
      return response;
  }
  
  @Path("/{id}")
  @PUT
  @Consumes({MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
  public Response update(@PathParam("id") String id, String userDetails)
  {
      Gson gson = AppUtility.initGSONBuilder().create();
      Response response = null;
      try
      {
          UserDetails details = gson.fromJson(userDetails, UserDetails.class);
          UserHandler userHandler = new UserHandler();
          details = userHandler.update(details, getSessionDataBean());

          response = Response.status(Status.CREATED.getStatusCode()).entity(details)
                  .type(MediaType.APPLICATION_JSON).build();
      }
      catch (CatissueException e)
      {
          LOGGER.error(e);
          response = getResponse(e);
      }
      catch (Exception e)
      {
          LOGGER.error(e);
          response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                  .header("errorMsg", e.getMessage()).type(MediaType.APPLICATION_JSON).build();
      }
      return response;
  }

  private SessionDataBean getSessionDataBean()
  {
      return (SessionDataBean) httpServletRequest.getSession().getAttribute(
              edu.wustl.catissuecore.util.global.Constants.SESSION_DATA);
  }
  
  private Response getResponse(CatissueException e)
  {
      SpecimenErrorCodeEnum errorCodeEnum = SpecimenErrorCodeEnum.getStatus(e.getErrorCode());
      int responseCode = getCorrespondingHTTPResponse(e.getErrorCode());
      Response response = Response.status(responseCode)
              .entity(e.getErrorCode() + ":" + errorCodeEnum.getDescription())
              .header("errorMsg", errorCodeEnum.getDescription()).type(MediaType.APPLICATION_JSON).build();
      return response;
  }

  private int getCorrespondingHTTPResponse(int errorCode)
  {
      int httpCode;
      switch (errorCode)
      {
          case 1004 :
              httpCode = Status.FORBIDDEN.getStatusCode();
              break;
          case 1003 :
          case 1011 :
          case 1013 :
          case 1014 :
          case 1015 :
          case 1016 :
              httpCode = Status.NOT_FOUND.getStatusCode();
              break;

          case 1005 :
          case 1006 :
          case 1007 :
          case 1008 :
          case 1009 :
          case 1010 :
          case 1012 :
          case 1029 :
          case 1040 :
          case 1050 :
              httpCode = Status.BAD_REQUEST.getStatusCode();
              break;
          default :
              httpCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
              break;
      }

      return httpCode;
  }
}
