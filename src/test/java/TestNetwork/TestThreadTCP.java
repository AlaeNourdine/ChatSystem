package TestNetwork;

import Network.TCPSend;

import java.io.IOException;

import Network.TCPReceive;

public class TestThreadTCP {
	
	public static void main (String arg[]) throws IOException {
		TCPSend t1;
		TCPReceive t2;
		//t1 = new TCP_client("sarra","10.1.5.148");
		t1 = new TCPSend(null, 0, null);
		t2 = new TCPReceive(0, null);
		t1.start();
		//t2.start();
		/*
		TCP_server.server();
		TCP_server.server();
		TCP_client.client("a");
		TCP_client.client("b");
		*/
	}
}
 