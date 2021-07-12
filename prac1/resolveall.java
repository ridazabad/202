import java.net.*;
import java.io.*;

//Main resolveall class obtains all IP from entered name
class resolveall{

	public static void main(String args[])
	{
		//if no arguements entered display usage line
		if(args.length == 0){	
			System.err.println("Usage:  resolve <name1> <name2> ... <nameN>");
			return;
		}
		//loops through all arguements provided
		for(int i = 0; i < args.length; i++){
		//getting the adress name
		String address = args[i];
		//Exception handling
		try{
			//get all IP from the given address
			InetAddress[] IP = InetAddress.getAllByName(address);
			//loops through the list containing all ip addresses
			for(int z = 0; z< IP.length; z++){
				//Display the address and the IP associated with it
				System.out.println(address + " : " + IP[z].getHostAddress());
			}
		}
		catch(Exception e) {
			//Error message
			System.err.println(address + " : Unknown Host");
		}
		}
	}
}
