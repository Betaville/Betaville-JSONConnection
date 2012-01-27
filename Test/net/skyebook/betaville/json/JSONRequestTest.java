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
	System.out.println("Betaville JSONClientManager Test file");
	}
	}


/* THIS IS THE JSON CLIENT TEST FILE - suppressing test code if needed in the future.


//Check User name - works
//Checking user name
 System.out.println(jcm.checkNameAvailability("ibby123"));
 
 
 
//Finding design by designID - works
Design design = jcm.findDesignByID(2364);
try {
System.out.println(design+" "+design.getDescription());
} catch(NullPointerException e) { System.out.println(""); }


//Fetching comments by designID - works
List<Comment> comment = jcm.getComments(835);
Design destiny = jcm.findDesignByID(835);
Iterator<Comment> it = comment.iterator();
try {
for(Comment temp: comment ) {
System.out.println(it.next().getComment());System.out.println();
System.out.println();
}
} catch(NullPointerException e) { System.out.println(""); }




//Fetching designs from server
long startTime = System.currentTimeMillis();
List<Design> designs = jcm.findAllDesignsByCity(2);
long endTime = System.currentTimeMillis();
try {
System.out.println(designs.size() + " designs retrieved in "+(endTime-startTime)/1000+" seconds");
} catch(NullPointerException e) { System.out.println(""); }



//Fetching design by name
List<Design> designsByName = jcm.findDesignsByName("dewqn");
try {
System.out.println(designsByName.size() + " designs retrieved");
} catch(NullPointerException e) { System.out.println(""); }




//Adding comments after starting session, works
jcm.startSession("ibby123", "123456");
Comment newcommie = new Comment(0,2404,"ibbyzj","This design looks really nice!");
jcm.addComment(newcommie);



//Removing a design if you own the Design, again need to start a session - works
jcm.startSession("ibby123", "123456");
jcm.removeDesign(1250);
 
 

*/