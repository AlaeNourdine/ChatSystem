package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.net.NetworkInterface;

/**
 * Classe braodcastUDP qui permet l'envoi en UDP
 *
 */

public class broadcastUDP {
	
	/**
	 * Constructeur broadcast UDP
	 */
	public broadcastUDP() {
	}
	
	/**
	 * Methode pour envoyer un message en broadcast
	 * 
	 * @param envoiebroadcast message a envoyer 
	 * @param port port a utiliser
	 * @throws IOException
	 */
    public static void envoiebroadcast(String broadcastMessage, int port) throws IOException {
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
     * Methode pour recuperer les adresses de broadcast des adresses de broadcast
     * 
     * @return la liste des adresses
     * @throws SocketException
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
    
    /**
     * Methode pour envoyer un message en udp
     * 
     * @param msg message a envoyer
     * @param port port a utiliser
     * @param laddr adresse a laquelle envoyer 
     * @throws SocketException
     */
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
