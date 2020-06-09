/* Name: Meecah Cahayon + Eunice Llobet
 * Student ID: 1259825 + 1330233
 */

import java.util.*;
import java.io.*;

public class Counter {
	
	public static void main(String[] args) {

		try {

			//READ AS STREAM OF BYTES
	        BufferedInputStream reader = new BufferedInputStream(System.in);

	        //String line = reader.readLine();
	        int bytes = reader.read();

	        while(bytes != -1) {

	        	System.out.write(bytes);
	        	bytes = reader.read();
        	}
		}
		catch(Exception eCounter) {

			System.out.println("Error " + eCounter);
			return;
		}
	}
}