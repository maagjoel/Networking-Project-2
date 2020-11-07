//package mydns;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author Victoria Lariot 6124058
 * @author Martin Alvarez 5856597
 * @author Gretel Gomez Rodriguez 6174028
 */
public class mydns {

    public int MAX_PACKET_SIZE = 512;
    private static String address;
    private static String name;
    private static int port = 53;
    private static String queryType = "A";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        // TODO code application logic here
        name = args[0];
        address = args[1];
        Response myResponse = makeRequest(address);
        myResponse.outputResponse();

        while (myResponse.getANCount() < 1) {
            myResponse = makeRequest(myResponse.getAdditionalRecords()[0].getDomain());
            myResponse.outputResponse();
        }

    }

    public static Response makeRequest(String address) throws SocketException, UnknownHostException, IOException {
        System.out.println("DNS Server to query: " + address);

        //Create Datagram socket and request object(s)
        DatagramSocket socket = new DatagramSocket();
        InetAddress inetaddress = InetAddress.getByName(address);

        Request request = new Request(name, queryType);

        byte[] requestBytes = request.getRequest();
        byte[] responseBytes = new byte[1024];

        DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, inetaddress, port);
        DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length);

        //Send packet and time response
        socket.send(requestPacket);
        socket.receive(responsePacket);
        socket.close();

        Response response = new Response(responsePacket.getData(), requestBytes.length, queryType);

        return response;

    }

}
