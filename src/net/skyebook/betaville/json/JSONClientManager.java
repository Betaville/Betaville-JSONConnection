/**  
 *  Betaville JSON Connection
 *  Copyright (C) 2011 Skye Book
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.skyebook.betaville.json;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ProposalPermission;
import edu.poly.bxmc.betaville.model.Wormhole;
import edu.poly.bxmc.betaville.model.IUser.UserType;
import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;
import edu.poly.bxmc.betaville.net.ProtectedManager;

/**
 * @author Skye Book
 *
 */
public class JSONClientManager implements ProtectedManager {

	private String baseURL = "http://localhost/service/service.php";

	// The user's authentication token for this session as assigned by the server
	private static String authToken;

	private JsonFactory jsonFactory;

	// Set to false if you'd prefer to have unzipped responses returned
	private boolean useGZIP = true;

	private static final String REQUEST_GZIP = "gz=1";

	public JSONClientManager(){
		this("http://localhost/service/service.php");
	}
	
	public JSONClientManager(String server){
		baseURL = server;
		jsonFactory = new JsonFactory();
	}

	private JsonParser doRequest(String request){
		return doRequest(request, false, null);
	}

	private JsonParser doRequest(String request, boolean requiresToken){
		return doRequest(request, requiresToken, null);
	}

	private JsonParser doRequest(String request, boolean requiresToken, PhysicalFileTransporter pft){
		try {
			URL url = null;

			// add gzip parameter if it is requested
			if(useGZIP){
				url = new URL(baseURL+"?"+request+"&"+REQUEST_GZIP);
			}
			else{
				url = new URL(baseURL+"?"+request);
			}
			

			// add token if this request requires it
			if(requiresToken){
				url = new URL(url.toString()+"&token="+authToken);
			}

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

			if(pft!=null){
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				OutputStream os = connection.getOutputStream();
				byte[] data = pft.getData();
				for(int i=0; i<data.length; i++){
					os.write(data[i]);
				}
			}

			return jsonFactory.createJsonParser(connection.getInputStream());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#isBusy()
	 */
	@Override
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#isAlive()
	 */
	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#checkNameAvailability(java.lang.String)
	 */
	@Override
	public boolean checkNameAvailability(String name) {
		String request = "section=user&request=available&username="+name;
		JsonParser response = doRequest(request);

		try {
			response.nextToken();
			while(response.nextToken()!=JsonToken.END_OBJECT){
				if(response.getCurrentName().equals("usernameAvailable")){
					response.nextToken();
					return response.getValueAsBoolean();
				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getUserEmail(java.lang.String)
	 */
	@Override
	public String getUserEmail(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignByID(int)
	 */
	@Override
	public Design findDesignByID(int designID) {
		String request = "section=design&request=findbyid&id="+designID;
		JsonParser response = doRequest(request);

		return JSONConverter.toDesign(response);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByName(java.lang.String)
	 */
	@Override
	public List<Design> findDesignsByName(String name) {
		String request = "section=design&request=findbyname&name="+name;
		JsonParser response = doRequest(request);

		return JSONConverter.toDesignList(response);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByUser(java.lang.String)
	 */
	@Override
	public List<Design> findDesignsByUser(String user) {
		String request = "section=design&request=findbyuser&user="+user;
		JsonParser response = doRequest(request);

		return JSONConverter.toDesignList(response);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByDate(java.util.Date)
	 */
	@Override
	public List<Design> findDesignsByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllDesignsByCity(int)
	 */
	@Override
	public List<Design> findAllDesignsByCity(int cityID) {
		String request = "section=design&request=findbycity&city="+cityID;
		JsonParser response = doRequest(request);
		return JSONConverter.toDesignList(response);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByCitySetStartEnd(int, boolean, int, int)
	 */
	@Override
	public List<Design> findDesignsByCitySetStartEnd(int cityID,
			boolean onlyBase, int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findBaseDesignsByCity(int)
	 */
	@Override
	public List<Design> findBaseDesignsByCity(int cityID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findTerrainByCity(int)
	 */
	@Override
	public List<Design> findTerrainByCity(int cityID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findVersionsOfProposal(int)
	 */
	@Override
	public int[] findVersionsOfProposal(int proposalDesignID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getProposalPermissions(int)
	 */
	@Override
	public ProposalPermission getProposalPermissions(int designID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllProposals(int)
	 */
	@Override
	public int[] findAllProposals(int designID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllProposalsNearLocation(edu.poly.bxmc.betaville.jme.map.UTMCoordinate, int)
	 */
	@Override
	public List<Design> findAllProposalsNearLocation(UTMCoordinate coordinate,
			int meterRadius) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestThumbnail(int)
	 */
	@Override
	public PhysicalFileTransporter requestThumbnail(int designID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestFile(edu.poly.bxmc.betaville.model.Design)
	 */
	@Override
	public PhysicalFileTransporter requestFile(Design design) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestFile(int)
	 */
	@Override
	public PhysicalFileTransporter requestFile(int designID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#addCity(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int addCity(String name, String state, String country) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllCities()
	 */
	@Override
	public List<City> findAllCities() {
		String request =  "section=city&request=getall";
		JsonParser response = doRequest(request,false,null);
		return JSONConverter.cityList(response);
		}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByName(java.lang.String)
	 */
	@Override
	public List<Integer> findCitiesByName(String name) {
		String request =  "section=city&request=findbyname&name="+name;
		JsonParser response = doRequest(request);
		List<City> tempcity = new ArrayList<City>();
		tempcity = JSONConverter.cityList(response);
		List<Integer> retint = new ArrayList<Integer>();
		Iterator<City> cityit = tempcity.iterator();
		for(int i = 0; i<tempcity.size(); i++) {
			retint.add(cityit.next().getCityID());
			}
		return retint;
		}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByState(java.lang.String)
	 */
	@Override
	public List<Integer> findCitiesByState(String state) {
		String request =  "section=city&request=findbystate&name="+state;
		JsonParser response = doRequest(request);
		List<City> tempcity = new ArrayList<City>();
		tempcity = JSONConverter.cityList(response);
		List<Integer> retint = new ArrayList<Integer>();
		Iterator<City> cityit = tempcity.iterator();
		for(int i = 0; i<tempcity.size(); i++) {
		retint.add(cityit.next().getCityID());
			}
		return retint;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByCountry(java.lang.String)
	 */
	@Override
	public List<Integer> findCitiesByCountry(String country) {
		String request =  "section=city&request=findbycountry&name="+country;
		JsonParser response = doRequest(request);
		List<City> tempcity = new ArrayList<City>();
		tempcity = JSONConverter.cityList(response);
		List<Integer> retint = new ArrayList<Integer>();
		Iterator<City> cityit = tempcity.iterator();
		for(int i = 0; i<tempcity.size(); i++) {
		retint.add(cityit.next().getCityID());
			}
		return retint;
	}		
	

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCityByID(int)
	 */
	@Override
	public String[] findCityByID(int cityID) {

		String request = "section=design&request=findbyid&id="+cityID;
		JsonParser response = doRequest(request);


		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCityByAll(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String[] findCityByAll(String name, String state, String country) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#reportSpamComment(int)
	 */
	@Override
	public void reportSpamComment(int commentID) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getComments(int)
	 */
	@Override
	public List<Comment> getComments(int designID) {
		
		String request = "section=comment&request=getforid&id="+designID;
		JsonParser response = doRequest(request);
		
		return JSONConverter.commentList(response);
		
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#checkUserLevel(java.lang.String, edu.poly.bxmc.betaville.model.IUser.UserType)
	 */
	@Override
	public int checkUserLevel(String user, UserType userType) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getDesignVersion()
	 */
	@Override
	public long getDesignVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getUserLevel(java.lang.String)
	 */
	@Override
	public UserType getUserLevel(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getWormholesWithin(edu.poly.bxmc.betaville.jme.map.UTMCoordinate, int, int)
	 */
	@Override
	public List<Wormhole> getWormholesWithin(UTMCoordinate location,
			int extentNorth, int extentEast) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getAllWormholes()
	 */
	@Override
	public List<Wormhole> getAllWormholes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getAllWormholesInCity(int)
	 */
	@Override
	public List<Wormhole> getAllWormholesInCity(int cityID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#synchronizeData(java.util.HashMap)
	 */
	@Override
	public List<Design> synchronizeData(HashMap<Integer, Integer> hashMap) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#authenticateUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticateUser(String name, String pass) {
		return startSession(name, pass);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#startSession(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean startSession(String name, String pass) {

		String request = "section=user&request=auth&username="+name+"&password="+pass;
		JsonParser json = doRequest(request);

		try {
			boolean success = false;
			
			json.nextToken();
			
			while(json.nextToken()!=JsonToken.END_OBJECT){
				
				if(json.getCurrentName().equals("authenticationsuccess")){
					json.nextToken();
					success = json.getBooleanValue();
				}
				else if(json.getCurrentName().equals("token")){
					json.nextToken();
					authToken = json.getText();
					System.out.println("Auth Token is: " + authToken);
				}
			}
			
			return success;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#endSession(java.lang.String)
	 */
	@Override
	public boolean endSession(String sessionToken) {
		String request = "section=user&request=endsession";
		JsonParser json = doRequest(request, true);

		try {
			boolean success = false;
			
			json.nextToken();
			
			while(json.nextToken()!=JsonToken.END_OBJECT){
				
				if(json.getCurrentName().equals("endsession")){
					json.nextToken();
					success = json.getBooleanValue();
				}
			}
			
			return success;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUser(String name, String pass, String email,
			String twitter, String bio) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changePassword(String name, String pass, String newPass) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeBio(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeBio(String name, String pass, String newBio) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addEmptyDesign(edu.poly.bxmc.betaville.model.EmptyDesign, java.lang.String, java.lang.String)
	 */
	@Override
	public int addEmptyDesign(EmptyDesign design, String user, String pass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addProposal(edu.poly.bxmc.betaville.model.Design, java.lang.String, java.lang.String, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.model.ProposalPermission)
	 */
	@Override
	public int addProposal(Design design, String removables, String user,
			String pass, PhysicalFileTransporter pft,
			PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter,
			ProposalPermission permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addVersion(edu.poly.bxmc.betaville.model.Design, java.lang.String, java.lang.String, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter)
	 */
	@Override
	public int addVersion(Design design, String removables, String user,
			String pass, PhysicalFileTransporter pft,
			PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addBase(edu.poly.bxmc.betaville.model.Design, java.lang.String, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter)
	 */
	@Override
	public int addBase(Design design, String user, String pass,
			PhysicalFileTransporter pft, PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter) {
		doRequest("section=design&request=addbase", true, pft);
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#setThumbnailForObject(int, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, java.lang.String, java.lang.String)
	 */
	@Override
	public int setThumbnailForObject(int designID, PhysicalFileTransporter pft,
			String user, String pass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#removeDesign(int, java.lang.String, java.lang.String)
	 */
	@Override
	public int removeDesign(int designID, String user, String pass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignName(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeDesignName(int designID, String user, String pass,
			String newName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignFile(int, java.lang.String, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, boolean)
	 */
	@Override
	public boolean changeDesignFile(int designID, String user, String pass,
			PhysicalFileTransporter pft, boolean textureOnOff) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignDescription(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeDesignDescription(int designID, String user,
			String pass, String newDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignAddress(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeDesignAddress(int designID, String user, String pass,
			String newAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignURL(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeDesignURL(int designID, String user, String pass,
			String newURL) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeModeledDesignLocation(int, float, java.lang.String, java.lang.String, edu.poly.bxmc.betaville.jme.map.UTMCoordinate)
	 */
	@Override
	public boolean changeModeledDesignLocation(int designID, float rotY,
			String user, String pass, UTMCoordinate newLocation) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#faveDesign(int, java.lang.String, java.lang.String)
	 */
	@Override
	public int faveDesign(int designID, String user, String pass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addComment(edu.poly.bxmc.betaville.model.Comment, java.lang.String)
	 */
	@Override
	public boolean addComment(Comment comment, String pass) {
				String request = "section=comment&request=add&designID="+comment.getCommentDesignID()+"&token="+authToken+"&comment="+URLEncoder.encode(comment.getComment());
				JsonParser json = doRequest(request,true);
				return true;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#deleteComment(int, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean deleteComment(int commentID, String user, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addWormhole(edu.poly.bxmc.betaville.jme.map.ILocation, java.lang.String, int)
	 */
	@Override
	public int addWormhole(ILocation location, String name, int cityID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#deleteWormhole(int)
	 */
	@Override
	public int deleteWormhole(int wormholeID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#editWormholeName(int, java.lang.String)
	 */
	@Override
	public int editWormholeName(int wormholeID, String newName) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#editWormholeLocation(int, edu.poly.bxmc.betaville.jme.map.UTMCoordinate)
	 */
	@Override
	public int editWormholeLocation(int wormholeID, UTMCoordinate newLocation) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<Design> getRecentProposals(int quantity){
		String request = "section=activity&request=proposals&quantity="+quantity;
		JsonParser response = doRequest(request);

		return JSONConverter.toDesignList(response);
	}
	public String getAuthToken() {
		return authToken;
	}

}
