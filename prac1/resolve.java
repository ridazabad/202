import java.net.*;
import java.io.*;

//Main resolve class obtains the IP from entered name
class resolve{

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
			//get the IP from the given address
			InetAddress IP = InetAddress.getByName(address);
			//Display the address and the IP associated with it
			System.out.println(address + " : " + IP.getHostAddress());
		}
		catch(Exception e) {
			//Error message
			System.err.println(address + " : Unknown Host");
		}
		}
	}
}
