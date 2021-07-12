import java.net.*;
import java.io.*;

class SimpleServer{
	public static void main(String args[]){
		//exception handling
		try{
			//get the IP adress from the computer the server is being created(for testing)
			InetAddress IP = InetAddress.getLocalHost();
			//create server socket
			ServerSocket ss = new ServerSocket(0);
			//print the serversocket details
			System.out.print(ss);
			//infinite loop
			while(true){
				//create socket client
				Socket client = ss.accept();
				//get teh IP of Client
				InetAddress IPc = client.getInetAddress();
				//Writer class
				PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
				//Get client details
				String clientN = IPc.getHostName();
				String clientA = IPc.getHostAddress();
				//write back
				writer.println("Hello " + clientN);
				//write back
				writer.println("Your Ip Address is " + clientA);
				//close the socket				
				client.close();
			}
		}
		catch(Exception e){	
			//Display error message
			System.err.println("Exception: " + e);
		}
	}
}
