/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.net.*;
import java.util.Arrays;

/**
 *
 * @author victorialariot
 */
public class Mydns {
    public int MAX_PACKET_SIZE = 512;
    //private static byte[] server = new byte[5];
    private static String address;
    private static String name;
    private static int port = 53;
    private static String queryType = "A";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        name = args[0];
        address = args[1];
        makeRequest();
    }
    
    public static void makeRequest() {
        System.out.println("DNS Server to query: " + address);
        
        try {

            //Create Datagram socket and request object(s)
            DatagramSocket socket = new DatagramSocket();
            InetAddress inetaddress = InetAddress.getByName(address);
           // server = inetaddress.getAddress();
            //System.out.println(Arrays.toString(server));
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
            response.outputResponse();

        } catch (SocketException e) {
            System.out.println("ERROR\tCould not create socket");
        } catch (UnknownHostException e ) {
            System.out.println("ERROR\tUnknown host");
        } catch (SocketTimeoutException e) {
            System.out.println("ERROR\tSocket Timeout");
            System.out.println("Reattempting request...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
