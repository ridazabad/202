import java.net.*;
import java.io.*;
import java.util.*;

class HttpServerSession extends Thread{
	
	//varaible
	private Socket connect = new Socket();
	//constructor
	public HttpServerSession(Socket s){
		connect = s;
	}
	//run method
	public void run(){
		try{	
			//Byte array
			byte[] bytearray = new byte[2000];
							
			//Buffered output
			BufferedOutputStream bos1 = new BufferedOutputStream(connect.getOutputStream());
	
			//Writer
			PrintWriter writer = new PrintWriter(connect.getOutputStream(), true);

			//Reader
			BufferedReader reader = new BufferedReader(
			new InputStreamReader(connect.getInputStream()));

			String line = reader.readLine();

			//List of requests
			String parts[] = line.split(" ");
			
			//Checks that there are 3
			if(parts.length == 3){
				//Checks teh first is GET
				if(parts[0].compareTo("GET") == 0){
					System.out.println("Request: " + parts[1]);
					//println(bos1, "HTTP/1.1 200 OK");
					//println(bos1, "");
					//println(bos1 , "Hello World");
					//make sure the contents of the file

				//Run through till the empty line or end of file
				while(true) {
					String line1 = reader.readLine();
					if(line1 == null) {
						System.out.println("End of the File");
						break;
					}
					if(line1.compareTo("") == 0)
						break;
					}
	
				//File Input stream
				String filename = parts[1].substring(1);
			
	
				File f = new File(filename);
				FileInputStream fis = null;
				int value;
				try{
					fis = new FileInputStream(f);
					println(bos1, "HTTP/1.1 200 OK");
					println(bos1, "Content-Length : " + f.length());
					println(bos1, "Accepted-Ranges : bytes");
					//println(bos1, f.LastModified());
					println(bos1, "");
					int x = fis.read(bytearray);
	
					while(x != -1){
							bos1.write(bytearray, 0, x);
							sleep(1000);
							x = fis.read(bytearray);
					}
					bos1.flush();
				}
				catch(Exception e){
					println(bos1, "HTTP/1.1 404 NOT FOUND");
					System.out.println("File Not found");
					println(bos1, "");
				}
				}
			}
			else{
				System.out.println("There must be three requests");
			}
	

			connect.close();
			
		}
		catch(Exception e){
			System.err.println("Error Occured: " + e);
		}
		
	}

	//New print method
	private void println(BufferedOutputStream bos, String s) throws IOException{
		String news = s + "\r\n";
		byte[] array = news.getBytes();
		for(int i = 0; i < array.length; i++){
			bos.write(array[i]);
		}
		bos.flush();
		return;
	}
	
}
class HttpServer{
	public static void main(String args[]){
		try{
			//ServerSocket on 8080
			ServerSocket ss = new ServerSocket(8080);
			//Print the port
			System.out.println(ss.toString());
			while(true){
				//Accept connection and print
				Socket client = ss.accept();
				System.out.println("Connection Received:");
				//Create thread with client
				HttpServerSession thread = new HttpServerSession(client);
				InetAddress IP = client.getInetAddress();
				System.out.println(IP.getHostAddress());
				thread.start();
				
			}
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
}

