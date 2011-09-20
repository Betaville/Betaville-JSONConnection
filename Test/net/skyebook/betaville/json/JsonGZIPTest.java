/**
 * 
 */
package net.skyebook.betaville.json;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

/**
 * @author Skye Book
 *
 */
public class JsonGZIPTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws JsonParseException 
	 */
	public static void main(String[] args) throws JsonParseException, MalformedURLException, IOException {
		JsonFactory factory = new JsonFactory();
		JsonParser plain= factory.createJsonParser(new URL("http://localhost/service/service.php?section=user&request=available&username=sbook"));
		JsonParser gzip= factory.createJsonParser(new URL("http://localhost/service/service.php?section=user&request=available&username=sbook&gz=1"));

		// parse plain:

		plain.nextToken();
		while(plain.nextToken()!=JsonToken.END_OBJECT){
			System.out.println(plain.getCurrentName() +":"+ plain.getText());
		}
		// parse plain:

		gzip.nextToken();
		while(gzip.nextToken()!=JsonToken.END_OBJECT){
			System.out.println(gzip.getCurrentName() +":"+ gzip.getText());
		}

	}
}