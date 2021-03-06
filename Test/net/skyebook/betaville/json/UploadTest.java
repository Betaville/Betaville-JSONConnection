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

import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;

/**
 * @author Skye Book
 *
 */
public class UploadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String contents = "blah blahseng s sidgsg nsg oinwetoiw43ntsong34teoigneh";
		
		PhysicalFileTransporter data = new PhysicalFileTransporter(contents.getBytes());
		JSONClientManager jcm = new JSONClientManager();
		System.out.println(jcm.addBase(null, null, null, data, null, null));
	}

}
