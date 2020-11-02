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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public void makeRequest() {
        System.out.println("DNS Server to query: " + address);
        
        
    }
}
