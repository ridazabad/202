import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.naming.ldap.*;
import javax.net.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;




class MyTLSFileServer{


	public static void main(String args[])
	{

		try{
				/*
		 * use the getSSF method to get a
		 * SSLServerSocketFactory and create our
		 * SSLServerSocket, bound to specified port
		 */

		 ServerSocketFactory ssf = getSSF();
		 SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(Integer.parseInt(args[0]));

		String EnabledProtocols[] = {"TLSv1.2", "TLSv1.1"};
 		ss.setEnabledProtocols(EnabledProtocols);



		SSLSocket ssls = (SSLSocket)ss.accept();
		BufferedOutputStream bos = new BufferedOutputStream(ssls.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(ssls.getInputStream()));
		String line = reader.readLine();
		reader = new BufferedReader(new InputStreamReader(ssls.getInputStream()));	
			  
		File file =  new File(line);


		if (!file.exists()){
			ssls.close();
		}
		else
		{
			byte[] bytearray = new byte[(int) file.length()]; 
			FileInputStream fis = new FileInputStream(file);
			printfile(bos, fis, bytearray);
			// prints the details of connection;
			// flush then close
			bos.flush();
			ssls.close();
		}
			    

		}
		
		catch(Exception e){
			//Error
			System.err.println("MyTLS Server : " + e);
		}


	}
	


	private static ServerSocketFactory getSSF() throws FileNotFoundException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException
	{

		/*
		* Get an SSL Context that speaks some version
		* of TLS, a KeyManager that can hold certs in
		* X.509 format, and a JavaKeyStore (JKS)
		* instance
		*/
		SSLContext ctx = SSLContext.getInstance("TLS");
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("JKS");

		/*
		* store the passphrase to unlock the JKS file.
		* completely insecure, there are better ways.
		* DON’T DO THIS!
		*/
		char[] passphrase = "nxaf3217".toCharArray();
		/*
		* load the keystore file.  The passhrase is
		* an optional parameter to allow for integrity
		* checking of the keystore.  Could be null
		*/
		ks.load(new FileInputStream("server.jks"), passphrase); 

		/*
		* init the KeyManagerFactory with a source
		* of key material.  The passphrase is necessary
		* to unlock the private key contained.
		*/
		kmf.init(ks, passphrase);
		/*
		* initialise the SSL context with the keys.
		*/
		ctx.init(kmf.getKeyManagers(), null, null);

		/*
		* get the factory we will use to create
		* our SSLServerSocket
		*/
		SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
		return ssf;

	}

	public static void printfile(BufferedOutputStream bos, FileInputStream fis, byte[] array) throws InterruptedException, IOException
	{
		// for array length
		int value;
		//Gets the values
		while((value = fis.read(array, 0, array.length)) != -1){
			bos.write(array, 0, value);
		}
		
		return;

	}

}
