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

import java.util.Iterator;
import java.util.List;

import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;

/**
 * A series of tests for the {@link JSONClientManager}
 * @author Skye Book
 *
 */
public class JSONRequestTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	JSONClientManager jcm = new JSONClientManager();
		//Checking user name
		System.out.println(jcm.checkNameAvailability("ibby123"));
		
		//Finding design by designID
		Design design = jcm.findDesignByID(2364);
		System.out.println(design+" "+design.getDescription());
		
		//Fetching designs from server
		long startTime = System.currentTimeMillis();
		List<Design> designs = jcm.findAllDesignsByCity(2);
		long endTime = System.currentTimeMillis();
		System.out.println(designs.size() + " designs retrieved in "+(endTime-startTime)/1000+" seconds");
		
		//Fetching design by name
		designs = jcm.findDesignsByName("Jan");
		System.out.println(designs.size() + " designs retrieved");
		
		//Fetching comments by designID
		List<Comment> comment = jcm.getComments(835);
		Design destiny = jcm.findDesignByID(835);
		/*Iterator<Comment> it = comment.iterator();
		for(Comment temp: comment ) {
			System.out.println(it.next().getComment());System.out.println();
			System.out.println();
		}*/
		
		//Adding comments after starting session, works.....
		//jcm.startSession("ibby123", "damn it");
		Comment newcommie = new Comment(0,835,"ibbyzj","This design looks really nice!");
		//jcm.addComment(newcommie, "damn it");
		System.out.println(comment.size()+" comments in "+destiny.getName());
		
		//Fetching Cities from Server ( not sure how important this is)
		List<City> newCity = jcm.findAllCities();
		System.out.println(newCity.size()+" cities fetched");
		Iterator<City> cityit = newCity.iterator();
		for(City temp: newCity ) {
			System.out.print(cityit.next().getCity()+", ");
			}
	
	}
	}