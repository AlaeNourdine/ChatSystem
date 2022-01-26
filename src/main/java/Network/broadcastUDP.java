package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class broadcastUDP {
	
	private static boolean online = true;
	
    public static boolean getConnexion() {
        return online;
    }
	
    public static void setConnexion(Boolean value) {
        online = value ;
    }
	
    private static boolean validNickname = true;
    
    public static boolean getvalidNickname() {
        return validNickname;
    }
    
    public static void setvalidNickname(Boolean value) {
        validNickname = value ;
    }
    
    public static InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while(nias.hasMoreElements()) {
                    InetAddress ia= (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof InetAddress) {
                        return ia;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("unable to get current IP " + e.getMessage());
        }
        return null;
    }
    
    
    public static void envoiBroadcast(String broadcastMessage) throws IOException {
        int port = 1234 ;
        for (InetAddress broadcastAddr : AllAddresses()) {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[]buffer = broadcastMessage.getBytes();
            DatagramPacket packet = new DatagramPacket( buffer, buffer.length,  broadcastAddr,port);
            socket.send(packet);
            socket.close();
            System.out.println("Broadcast sent with address " + broadcastAddr.toString());
            System.out.println("***********Message envoye***********");
            System.out.println("Dest Ip: " + broadcastAddr.toString());
            System.out.println("Dest port: " + String.valueOf(port));
            System.out.println("Contenu: ");
            System.out.println(broadcastMessage);
            System.out.println("************************************");
        }
    }

	private static List<InetAddress> AllAddresses() throws SocketException {
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
    

}
