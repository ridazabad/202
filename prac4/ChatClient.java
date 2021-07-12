import java.net.*;
import java.io.*;
import java.util.*;

class ChatClient
{

	public static void main(String args[]){

		ChatClient chat = new ChatClient();
		chat.start_server();


	}

	public void start_server(){

		try{
			//Main Address and Port
			String Address = "239.0.202.1";
			int Port = 40202;
			//Create the multicastSocket
			MulticastSocket mcs = new MulticastSocket(Port);
			InetAddress IP = InetAddress.getByName(Address);
			mcs.joinGroup(IP);			
			//System.out.println("Sup");

			ChatClientReader read1 = new ChatClientReader(mcs);
			read1.start();
			
			for(;;){

				//Reader
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String line = reader.readLine();
	
				//Check if not empty
				if(line != null){
			
					byte[] array = line.getBytes();
					DatagramPacket msgP = new DatagramPacket(array, array.length, IP, Port);
					mcs.send(msgP);
					//System.out.println(mcs.getLocalPort() + ": " + line);
			
				}
			}

		}

		catch(Exception e){
			//Write the error
			System.err.println("Error Occured: " + e);
		
		}
	}
	 	
}

class ChatClientReader extends Thread{
	
	private MulticastSocket mcs;

	public ChatClientReader(MulticastSocket x){

		mcs = x;
	}

	public void run(){

		
		try{
			while(true){
				
				//Recieve the input
				byte[] bytearray = new byte[1000];
				DatagramPacket dR = new DatagramPacket(bytearray, bytearray.length);
				mcs.receive(dR);
				int port = dR.getPort();
				InetAddress IPR = dR.getAddress();

				//Read the message
				String messageR = new String(dR.getData(), 0, dR.getLength());
				System.out.println(IPR + ": " + messageR);
			}
			

		}
		catch(Exception e){
			//Write the error
			System.err.println("Error Occured: " + e);
		}
	}

}
