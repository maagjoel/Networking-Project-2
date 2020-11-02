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
 * @author victorialariot
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
		byte[] randomID = new byte[2]; 
		new Random().nextBytes(randomID);
		header.put(randomID);
		header.put((byte)0x01);
		header.put((byte)0x00);
		header.put((byte)0x00);
		header.put((byte)0x01);
		
		//lines 3, 4, and 5 will be all 0s, which is what we want
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
		question.put(hexStringToByteArray("000" + hexValueFromQueryType(type)));
		question.put((byte) 0x00);
		//Add Query Class - always  0x0001 for internet addresses
		question.put((byte) 0x0001);

		return question.array();
	}
}
