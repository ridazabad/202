import java.net.*;
import java.io.*;
import java.util.*;

class TftpClient extends Thread
{

	public static void main(String args[]){
	
		try{
			//RRQ is 1
			byte RRQ = 1;
			byte DATA = 2;
			byte ACK = 3;	
			byte ERROR = 4;
			FileOutputStream fos;
			
			//create the byte array
			byte[] array = new byte[1472];
			//get the opcode and filename
			array = Request(RRQ, args[1]);

			File f = new File(args[1]);
			fos = new FileOutputStream(f);
			
			//get the port number
			int x = Integer.parseInt(args[0]);
			
			//Get where it needs to be sent IP
			InetAddress IP = InetAddress.getByName(args[2]);
	
			//Create Socket
			DatagramSocket ds = new DatagramSocket();

			//Create Packet
			DatagramPacket dp = new DatagramPacket(array, array.length, IP, x);
			
			//Send
			ds.send(dp);

			//Counter Varaible 
			int v = 0;

			for(;;){
				//Check the file
				byte[] arrayR = new byte[1472];
				DatagramPacket dpR = new DatagramPacket(arrayR, 1472);
			
				ds.receive(dpR);
				
				//Size of the datapacket
				int size = dpR.getLength();
				
				arrayR = dpR.getData();
				
				if(arrayR[0] == ERROR){
					
					String err = new String(arrayR, 1, dpR.getLength() - 1);
					System.out.println(err);
					break;
				}
				else if(arrayR[0] == DATA){


					//Display
					String Data = new String(arrayR, 2, dpR.getLength());
					System.out.println(Data);

					//get the location
					int x1 = dpR.getPort();
					InetAddress IP1 = dpR.getAddress();
				
					//inc
					v= v + 1;

					System.out.println("---" + Integer.toString(v) + "---");
								

					//ACK
					byte [] arrayACK = ACKRequest(ACK, (byte)v);
						
					//Datagram Packet
					DatagramPacket dpACK = new DatagramPacket(arrayACK, arrayACK.length, IP1, x1);
					//Send
					ds.send(dpACK);

					//print into file
					byte[] filecontent = new byte[size - 2];
					
					//Copy
					System.arraycopy(arrayR, 2, filecontent, 0, dpR.getLength()-2);

					//Into file
					fos.write(filecontent);

					if(size < 512){

					//Checks if the end of the file 
					break;
					
					}
					
					
				}
				
			}			

		}	
		catch(Exception e){
	
		}
	}
		
	public static byte[] Request(byte requestType, String filename){
		
		byte[] file = new byte[1 + filename.length()];
		//First byte opcode
		file[0] = requestType;

		//Save the filename into byte array
		for(int i = 1; i< file.length ; i++){

			file[i] = (byte)filename.charAt(i - 1);
		}
		
		return file;
	}

	public static byte[] ACKRequest(byte requestType, byte packageNu){
		
		byte[] file = new byte[2];
		//First byte opcode
		file[0] = requestType;
		file[1] = packageNu;
		
		return file;
	}
	 	
}
