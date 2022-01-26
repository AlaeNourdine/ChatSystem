 package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceive extends Thread {
    
    private final int serverPort ;
    private final String fileOutput;
    private ServerSocket serverSocket;
    private  Socket connexionSocket;
    
    public TCPReceive( int port, String fileReceived) throws IOException{
       super();
        serverPort=port;
        fileOutput=fileReceived;
        serverSocket = null;
        connexionSocket=null;
    }
    
    @Override
    public  void run() {
    	System.out.println("----file ready to be received: "+fileOutput);  
    	byte[] aByte = new byte[1];
        	int bytesRead;

        	InputStream IS = null;

        	try {
        		serverSocket = new ServerSocket(serverPort);
        		connexionSocket = serverSocket.accept();
                                

        		IS = connexionSocket.getInputStream();
            
        	} catch (IOException ex) {
            // Do exception handling
        	}

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        if (IS != null) {

            FileOutputStream FOS = null;
            BufferedOutputStream BOS = null;
            try {
                FOS = new FileOutputStream( fileOutput );
                BOS = new BufferedOutputStream(FOS);
                bytesRead = IS.read(aByte, 0, aByte.length);

                do {
                        BAOS.write(aByte);
                        bytesRead = IS.read(aByte);
                } while (bytesRead != -1);

                BOS.write(BAOS.toByteArray());
                BOS.flush();
                BOS.close();
                serverSocket.close();
                System.out.println("----File received: "+fileOutput);
                System.out.println("----Thread TCP Receive closed");
                //Envoyer un signal pour le NI pour lui dire qu'on a bien recu le fichier
                //=> ecrire sur la fenêtre File received & => envoyer l'information à l'emetteur? (plus dur)
            } catch (IOException ex) {
                // Do exception handling
            }
        }
    }
    
    
}
