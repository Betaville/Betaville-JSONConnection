/**
 * 
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
		System.out.println(jcm.addBase(null, null, null, data, null));
	}

}
