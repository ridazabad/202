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
import javax.naming.InvalidNameException;

class MyTLSclient
{
    public static void main(String args[]) throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException , InvalidNameException
	    {
	   
       	   InputStream inps = null;
	   OutputStream ops =  null;
	   
	   
	SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
	SSLSocket socket = (SSLSocket)factory.createSocket(args[0], Integer.parseInt(args[1]));
	 

	 /*
	 * at this point, can getInputStream and
	 * getOutputStream as you would a regular Socket
	 */ 

	 
	  /*
	 * set HTTPS-style checking of HostName
	 * before the handshake commences
	 */
	 
	 SSLParameters params = new SSLParameters();
	 params.setEndpointIdentificationAlgorithm("HTTPS");
	 socket.setSSLParameters(params);

	 
	 socket.startHandshake();
	  
	SSLSession session = socket.getSession();
	X509Certificate cert = (X509Certificate)session.getPeerCertificates()[0];

	inps = socket.getInputStream();
	

	String filename = args[2];
	File file = new File(filename);
	inps = new FileInputStream(file);
	ops = new FileOutputStream(new File("1" + filename));
	PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
	writer.println(filename);

	while (socket.isConnected())
	{
	int bytesRead;  
	byte[] aByte = new byte[1024];


		while ( (bytesRead = inps.read(aByte)) >0   ) {
		    ops.write(aByte, 0, bytesRead); 
		}  
		ops.flush();
	       ops.close();
	 break;


	}
}
    
	static String getCommonName(X509Certificate cert) throws InvalidNameException
	{
		String name = cert.getSubjectX500Principal().getName();
		LdapName ln = new LdapName(name);
		String cn = null;
		for(Rdn rdn : ln.getRdns())
		if("CN".equalsIgnoreCase(rdn.getType())){
		 	cn = rdn.getValue().toString();
		}
	
		return cn;
	} 
    
    
 }
    
    
    
    
    
