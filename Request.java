/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 *
 * @author gretel
 */
public class Request {
    
    private String domain;
	private String type;
        
	public Request(String domain, String type){
		this.domain = domain;
		this.type = type;
	}
        
        public byte[] getRequest(){
		int qNameLength = domain.length() + 1;
		ByteBuffer request = ByteBuffer.allocate(12 + 5 + qNameLength);
		request.put(createRequestHeader());
		request.put(createQuestionHeader(qNameLength));
        return request.array();
	}
        
        private byte[] createRequestHeader(){
		ByteBuffer header = ByteBuffer.allocate(12);
		//byte ID = new byte[123]; 
		//new Random().nextBytes(ID);
		//header.put(ID);
                header.put((byte)0x13);
                header.put((byte)0x50);
		header.put((byte)0x00);
		header.put((byte)0x00);
		header.put((byte)0x00);
		header.put((byte)0x01);
                header.put((byte)0x00);
		header.put((byte)0x00);
                header.put((byte)0x00);
		header.put((byte)0x00);
                header.put((byte)0x00);
                header.put((byte)0x00);
		
		return header.array();
	}
        
        private byte[] createQuestionHeader(int qNameLength){
		ByteBuffer question = ByteBuffer.allocate(qNameLength+5);
		
		//first calculate how many bytes we need so we know the size of the array
		String[] items = domain.split("\\.");
		for(int i=0; i < items.length; i ++){
			question.put((byte) items[i].length());
			for (int j = 0; j < items[i].length(); j++){
				question.put((byte) ((int) items[i].charAt(j)));
				
			}
		}
            question.put((byte) 0x00);

		//Add Query Type
                //if (type.equals("A")) {
                    question.put((byte) 0x00);
                //} else if (type.equals("NS")) {
                   // question.put((byte) 0x02);
               // } else{
                  //  System.out.println("INVALID QUERY TYPE");
                //}
		question.put((byte) 0x01);
                
		//Add Query Class - always  0x0001 for internet addresses
		question.put((byte) 0x00);
                question.put((byte) 0x01);

		return question.array();
	}
}