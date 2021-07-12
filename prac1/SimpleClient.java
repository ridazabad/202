import java.net.*;
import java.io.*;

class SimpleClient{
	public static void main(String args[]){
		//if no arguements entered display usage line
		if(args.length != 2){	
			System.err.println("Usage:  <Hostname> <Port Number>");
			return;
		}
		try{
			//get the IP address from the client computer
			InetAddress IP = InetAddress.getLocalHost();
			//get the IP address of the server
			InetAddress IPserver = InetAddress.getByName(args[0]);
			//save the port
			String x = args[1];
			//Create Socket
			Socket me = new Socket(IPserver, Integer.parseInt(x));
			//Reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(me.getInputStream()));
			//read the line
			String line = reader.readLine();
			//read the line
			String line1 = reader.readLine();
			//close the socket
			me.close();
			//Write the line
			System.out.println(line);
			System.out.println(line1);
		}
		catch(Exception e){
			//Error message
			System.err.println(e);
		}
		
	}
}
