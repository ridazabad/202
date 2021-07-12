import java.net.*;
import java.io.*;

//Main reverse class obtains the DNS name from entered IP
class reverse{

	public static void main(String args[])
	{
		//if no arguements entered display usage line
		if(args.length == 0){	
			System.err.println("Usage:  resolve <IP1> <IP2> ... <IPN>");
			return;
		}
		//loops through all arguements provided
		for(int i = 0; i < args.length; i++){
		//getting the adress name
		String address = args[i];
		//Exception handling
		try{
			//get the IP from the given adsress
			InetAddress IP = InetAddress.getByName(address);
			//checks if the it is returning the IP address and not the DNS name
			if(address.compareTo(IP.getHostName()) == 0){
				//Display IP adress and no name
				System.out.println(address + " : no name");
			}
			else{
				//Display the Ip adress and the DNS name
				System.out.println(address + " : " + IP.getHostName());
			}
		}
		catch(Exception e) {
			//Error message
			System.err.println(address + " : Unknown Host");
		}
		}
	}
}
