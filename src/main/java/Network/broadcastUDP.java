package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


/**
 * Class permettant l'envoi en udp
 *
 */

public class broadcastUDP {
	
	//Constructor
	public broadcastUDP() {
	}
	
    public static void envoibroadcast(String broadcastMessage, int port) throws IOException {
		for (InetAddress  addrbroadcast : listAllBroadcastAddresses()) {
    		DatagramSocket socket = new DatagramSocket();
    		socket.setBroadcast(true);
    		byte[] buffer = broadcastMessage.getBytes();
    		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addrbroadcast, port);
    		//System.out.println("Envoi msg en broadcast to"+addrbroadcast);
    		socket.send(packet);
    		socket.close();
		}
    }
    
    
    /**
     * Récupération des adresses de broadcast
     */
    public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces 
          = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream() 
              .map(a -> a.getBroadcast())
              .filter(Objects::nonNull)
              .forEach(broadcastList::add);
        }
        return broadcastList;
    }
    
 
    public static void sendUDP(String msg, int port, String laddr) throws SocketException {

    	DatagramSocket socket = new DatagramSocket();

        byte[] buffer = msg.getBytes();
        DatagramPacket packet;
		try {
			packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(laddr), port);
			//System.out.println("Envoi msg");
			socket.send(packet);
        	socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
