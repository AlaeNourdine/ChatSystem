package Network;

import java.net.*;
import java.io.*;
import Model.Messages;


public class UDPSend {
    
    private DatagramSocket _socket;
    private NetworkInterface _chat;
    
    public UDPSend(NetworkInterface father) throws SocketException{
        set_chat(father);
        _socket = new DatagramSocket();
    }
    
    public void sendTo(Messages m, String hostName, int DesPort) {
        try {
            InetAddress address = InetAddress.getByName(hostName);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
            os.flush();
            os.writeObject(m);
            os.flush();
            //retrieves byte array
            byte[] sendBuf = byteStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, DesPort);
            int byteCount = packet.getLength();
            _socket.send(packet);
            os.close();
        }
        catch (UnknownHostException e)
        {
          System.err.println("Exception:  " + e);
        }
        catch (IOException e)    {}
  }

	public NetworkInterface get_chat() {
		return _chat;
	}

	public void set_chat(NetworkInterface _chat) {
		this._chat = _chat;
	}
}