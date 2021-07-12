import java.net.*;
import java.io.*;
import java.util.*;

class TftpServerWorker extends Thread
{
    private DatagramPacket req;
    private static final byte RRQ = 1;
    private static final byte DATA = 2;
    private static final byte ACK = 3;
    private static final byte ERROR = 4;

    private void sendfile(String filename)
    {
        /*
         * open the file using a FileInputStream and send it, one block at
         * a time, to the receiver.
         */
	byte[] bytearray = new byte[514];

	try{
		//Socket
		DatagramSocket dsWorker = new DatagramSocket();		
		
		//Get the file and create File Input
		File f = new File(filename);
		System.out.println(filename);
		
		if(f.exists()){

			//Create byte array to store
			byte[] arrayData = new byte[512];

			//get the location
			DatagramPacket receivedP1 = this.req;
			int x1 = receivedP1.getPort();
			InetAddress IP1 = receivedP1.getAddress();			

			FileInputStream fis = null;
			DatagramSocket ds = new DatagramSocket();
			//Read the file
			fis = new FileInputStream(f);
			int rc = fis.read(arrayData);
			int packageNumber = 1;

			//input into main array and send
			bytearray = DataRequest(DATA, (byte)packageNumber, arrayData);

			DatagramPacket First = new DatagramPacket(bytearray, bytearray.length, IP1, x1);

			dsWorker.send(First);

			while(rc != -1){

				
				//get next
				rc = fis.read(arrayData);
				
				byte[] arrayACK = new byte[2];
				DatagramPacket ACKP = new DatagramPacket(arrayACK, 2);
				
				dsWorker.receive(ACKP);

				arrayACK = ACKP.getData();
			
				if(arrayACK[0] == ACK){

				if( rc == 512 ){
						
				
					//Inc the value
					packageNumber = packageNumber + 1;
				
					//input into main array and send
					bytearray = DataRequest(DATA, (byte)packageNumber, arrayData);

					//DatagramPacket
					DatagramPacket FileData = new DatagramPacket(bytearray, bytearray.length, IP1, x1);

					//DataSocket to send
					dsWorker.send(FileData);
				
					//Send packet to the client
					DatagramPacket receivedP = this.req;
					int x = receivedP.getPort();
					InetAddress IP = receivedP.getAddress();
				}

				else if( rc == -1){
					
					//Done
					System.out.println("Done");
					break;


				}

		
				else if( rc < 512 ) {
	


					//Send the last remaining bytes
					//Inc the value
					packageNumber = packageNumber + 1;
		
					//New byte array
					byte[] last = new byte[rc];
					
					for(int q = 1; q < rc; q++){

						last[q] = arrayData[q];
					}		

					
				
					//input into main array and send
					bytearray = DataRequest(DATA, (byte)packageNumber, last);

					//DatagramPacket
					DatagramPacket FileData = new DatagramPacket(bytearray, bytearray.length, IP1, x1);

					//Send
					dsWorker.send(FileData);

					
	
				}

				}
			
				
			}
			
				

			}
		else{
			//Send packet to the client
			DatagramPacket receivedP = this.req;
			int x = receivedP.getPort();
			InetAddress IP = receivedP.getAddress();
			
			//Send back
			bytearray = Request(ERROR, "File Not Found");
			
			System.out.println("File Not Found");

			DatagramPacket Error1 = new DatagramPacket(bytearray, bytearray.length, IP, x); 
			dsWorker.send(Error1);		
		
		}
	
	}
	catch(Exception e){
		//Display error
		System.err.println(e);

	}
	return;
    }

    public void run()
    {
        /*
         * parse the request packet, ensuring that it is a RRQ
         * and then call sendfile
         */
	
	DatagramPacket receivedP = this.req;
	byte[] bytearray = new byte[receivedP.getLength()];
	bytearray = receivedP.getData();
	if(bytearray[0] == RRQ){
		String filename = new String(bytearray, 1, receivedP.getLength() - 1);
		File test = new File(filename);

		sendfile(filename);
	}

	return;
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

	public static byte[] DataRequest(byte requestType, byte blockNu, byte[] array){
		
		byte[] file = new byte[2 + array.length];
		//First byte opcode
		file[0] = requestType;
		file[1] = blockNu;

		//Save the filename into byte array
		for(int i = 2; i<= array.length + 1 ; i++){

			file[i] = array[i-2];
		}
		
		return file;
	}


    public TftpServerWorker(DatagramPacket req)
    {
	this.req = req;
    }
}

class TftpServer
{
    public void start_server()
    {
	try {
	    DatagramSocket ds = new DatagramSocket();
	    System.out.println("TftpServer on port " + ds.getLocalPort());

	    for(;;) {
		byte[] buf = new byte[1472];
		DatagramPacket p = new DatagramPacket(buf, 1472);
		ds.receive(p);
		
		TftpServerWorker worker = new TftpServerWorker(p);
		worker.start();
	    }
	}
	catch(Exception e) {
	    System.err.println("Exception: " + e);
	}

	return;
    }

    public static void main(String args[])
    {
	TftpServer d = new TftpServer();
	d.start_server();
    }
}
