package Network;

import java.io.*;
import java.net.Socket;


public class TCPSend extends Thread {
    private final File fileToSend;    
    private Socket connectionSocket;
    private BufferedOutputStream outToClient;
    private final int port;
    private final String ipServer;
    
    public TCPSend(File fileSend,int p,String ipAddress){
        super();
       fileToSend=fileSend;
       port=p;
       ipServer=ipAddress;
       connectionSocket = null;
       outToClient = null;
       
    }

   @Override
   public void run() {

        while (true) {
          
            try {
                
                System.out.println("----File ready to send:" +fileToSend.getName());
                this.connectionSocket = new Socket(ipServer,port);
                
                this.outToClient = new BufferedOutputStream(this.connectionSocket.getOutputStream());
            } catch (IOException ex) {
                // Do exception handling
            }

            if (outToClient != null) {
                File myFile = this.fileToSend;
                if (myFile.exists()){
                System.out.println("----File exists: "+fileToSend.getName());
                }
                else{
                    System.out.println("------------\n ERROR File does not exists anymore \n ------------");
                    System.exit(1);
                }
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream FIS = null;

                try {
                    FIS = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                    // Do exception handling
                }
                BufferedInputStream BIS = new BufferedInputStream(FIS);

                try {
                    BIS.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();
                    System.out.println("----File sended: " +fileToSend.getName());
                    System.out.println("----Thread TCP Send Closed");

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                    // Do exception handling
                }
            }
        }
    }
    
   
    
}
