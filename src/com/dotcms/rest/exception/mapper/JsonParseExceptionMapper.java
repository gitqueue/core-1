package com.dotcms.rest.exception.mapper;

import com.dotcms.repackage.com.fasterxml.jackson.core.JsonParseException;
import com.dotcms.repackage.com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.repackage.javax.ws.rs.ext.ExceptionMapper;
import com.dotcms.repackage.javax.ws.rs.ext.Provider;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.json.JSONException;
import com.dotmarketing.util.json.JSONObject;

/**
 * Created by Oscar Arrieta on 8/27/15.
 *
 * Instead of returning 5xx error, we need a way to tell the user is their fault that the Rest call is not working.
 * With this class we catch JsonParseException and print our own status error and message in order to
 * alert the user they are sending incorrect data and should fix it before try again.
 */
@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException exception)
    {
        //Log into our logs first.
        Logger.warn(this.getClass(), exception.getMessage(), exception);
        String msg = exception.getMessage();

        //Creating the message in JSON format.
        String entity;
        try {
            JSONObject json = new JSONObject();
            json.put("error", msg);
            entity = json.toString();
        } catch (JSONException e) {
            entity = "{ \"error\": \"" + msg.replace("\"", "\\\"") + "\" }";
        }

        //Return 4xx message to the client.
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(entity)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
