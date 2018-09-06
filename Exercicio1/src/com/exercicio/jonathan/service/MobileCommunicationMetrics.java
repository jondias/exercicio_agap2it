package com.exercicio.jonathan.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.exercicio.jonathan.model.MetricRequest;
import com.exercicio.jonathan.util.Constant;
import com.exercicio.jonathan.util.MessageValidator;

@Path("/")
public class MobileCommunicationMetrics {

    private static String SEARCH_DATE;
    private static long PROCESSED_JSON_FILES;
    private static long PROCESSED_ROWS;
    private static long PROCESSED_CALLS;
    private static long PROCESSED_MSGS;
    private long rowWithMissingField;
    private long messageWithBlankContent;
    private double okCall;
    private double koCall;

    public static String getDate() {
	return SEARCH_DATE;
    }

    public static void setDate(String date) {
	SEARCH_DATE = date;
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMetrics() {

	if (SEARCH_DATE== null || SEARCH_DATE.trim().isEmpty()) {
	    return Response.status(200).entity("Please call http://localhost:8080/Exercicio1/api/setDate using a tool like SoapUi to set up the date before call this service.").build();
	}
	StringBuilder result = new StringBuilder();
	BufferedReader buffReader = null;
	boolean isFound = Boolean.FALSE;
	try {
	    File dir = new File(Constant.LOGS_DIR);
	    File[] listOfFiles = dir.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isFile()) {
		    if (listOfFiles[i].getName().contains(SEARCH_DATE)) {
			isFound = Boolean.TRUE;
			PROCESSED_JSON_FILES++;
			InputStream inputStream = new FileInputStream(listOfFiles[i]);
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			buffReader = new BufferedReader(streamReader);
			String line;
			while ((line = buffReader.readLine()) != null) {
			    PROCESSED_ROWS++;
			    JSONObject jsonObject = new JSONObject(line);
			    try {
				if (jsonObject.get(Constant.MESSAGE_TYPE).equals(Constant.MESSAGE_CALL)) {
				
				if (!MessageValidator.hasValidCommonFields(jsonObject) && !MessageValidator.hasValidCallFields(jsonObject)) {
				    rowWithMissingField++;
				} else {
				    if(jsonObject.get(Constant.MESSAGE_STATUS_CODE).equals(Constant.MESSAGE_STATUS_OK)) {
					okCall++;
				    } else {
					koCall++;
				    }
				}
				PROCESSED_CALLS++;

				} else if (jsonObject.get(Constant.MESSAGE_TYPE).equals(Constant.MESSAGE_MSG)) {
				if (!MessageValidator.hasValidCommonFields(jsonObject) && !MessageValidator.hasValidMessageFields(jsonObject)) {
				    rowWithMissingField++;
				} else {
				    
				    if(jsonObject.get(Constant.MESSAGE_CONTENT).toString().trim().isEmpty())
					messageWithBlankContent++;
					
				}
				PROCESSED_MSGS++;

				} else {
				//Couting empty rows and messages without message type as row with missing field.
				rowWithMissingField++;
				}
			    } catch (org.json.JSONException e) {
				    rowWithMissingField++;
				    e.printStackTrace();
			    } 
			}

		    }
		}
	    }
	    
	    if (isFound) {
		result.append("MobileCommunication - Metrics Successfully processed." + "\n");
		result.append("Number of rows with missing fields: " + rowWithMissingField + "\n");
		result.append("Number of messages with blank content: " + messageWithBlankContent + "\n");
		
		koCall = koCall==0?1:koCall;
		result.append("Relationship between OK/KO calls: " + (okCall / koCall) + "\n");
	    } else {
		result.append("MobileCommunication - Metrics Successfully processed." + "\n");
		result.append("File not found!" + "\n");
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}  finally {
	    if (buffReader != null) {
		try {
		    buffReader.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	   

	return Response.status(200).entity(result.toString()).build();
    }

    @POST
    @Path("/setDate")
    @Consumes(MediaType.APPLICATION_XML)
    public Response setPeriod(MetricRequest dateFormat) {
	SEARCH_DATE = dateFormat.getFileDate();
	return Response.status(200).entity(SEARCH_DATE).build();
    }
    
    @GET
    @Path("/kpis")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getKpis() {
	
	StringBuilder result = new StringBuilder();
	result.append("MobileCommunication - Metrics Successfully processed."+"\n");
	result.append("Total number of processed JSON files: "+ PROCESSED_JSON_FILES +"\n");
	result.append("Total number of rows: "+ PROCESSED_ROWS +"\n");
	result.append("Total number of calls: "+ PROCESSED_CALLS +"\n");
	result.append("Total number of messages: "+ PROCESSED_MSGS +"\n");
	return Response.status(200).entity(result.toString()).build();
	
    }

}
