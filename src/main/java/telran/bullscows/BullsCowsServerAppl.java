package telran.bullscows;

import telran.net.Protocol;
import telran.net.TcpServer;

public class BullsCowsServerAppl {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		BullsCowsService bullscows = new BullsCowsMapImpl();
		Protocol protocol = new BullsCowsProtocol(bullscows);
		TcpServer tcpServer = new TcpServer(protocol, PORT);
		tcpServer.run();

	}

}
