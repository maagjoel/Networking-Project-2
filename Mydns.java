/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
/**
 *
 * @author victorialariot
 */
public class Mydns {
    public int MAX_PACKET_SIZE = 512;
    private byte[] server = new byte[4];
    String address;
    private String name;
    private int port = 53;
    String queryType = "A";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public void makeRequest() {
        System.out.println("DNS Server to query: " + address);
        
        try {
            //Create Datagram socket and request object(s)
            DatagramSocket socket = new DatagramSocket();
            InetAddress inetaddress = InetAddress.getByAddress(server);
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
            pollRequest(++retryNumber);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }
}
