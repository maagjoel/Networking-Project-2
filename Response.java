/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 *
 * @author gretel
 */
public class Response {
    private byte[] response;
    private byte[] ID;
    private boolean QR, AA, TC, RD, RA;
    private int RCode, QDCount, ANCount, NSCount, ARCount;

       public int getANCount() {
        return ANCount;
    }
    
    private String queryType;
    private boolean noRecords = false;
    private Records[] answerRecords;
    private Records[] authorativeRecords;
    private Records[] additionalRecords;

    public Records[] getAuthorativeRecords() {
        return authorativeRecords;
    }
    
    public Response(byte[] response, int requestSize, String queryType) {
		this.response = response;
		this.queryType = queryType;

        this.validateResponseQuestionType();
        this.parseHeader();  
        
        int offSet = requestSize;
        
        if (ANCount != 0){
        answerRecords = new Records[ANCount];
        
            for(int i = 0; i < ANCount; i ++){
        	answerRecords[i] = this.parseAnswer(offSet);
        	offSet += answerRecords[i].getByteLength();
            }
            
        }
        
        if (NSCount != 0) {
            authorativeRecords = new Records[NSCount];
            for(int i = 0; i < NSCount; i ++){
        	authorativeRecords[i] = this.parseAnswer(offSet);
        	offSet += authorativeRecords[i].getByteLength();
            }
        }
        
        if (ARCount !=0) {
            additionalRecords = new Records[ARCount];
            for(int i = 0; i < ARCount; i++){
        	additionalRecords[i] = this.parseAnswer(offSet);
               	offSet += additionalRecords[i].getByteLength();
            }   
        }
        
        
        
        
    }
    
    public void outputResponse() {
        System.out.println();
        System.out.println("Reply received. Content overview:");
        System.out.println(ANCount + " Answers.");
        System.out.println(NSCount + " Intermediate Name Servers.");
        System.out.println(ARCount + " Additional Information Records.");
        
        System.out.println("Answer Section:");
       
        if(this.ANCount >0){
            for (Records record : answerRecords){
        	record.outputRecord();	
            }
        }
        

        if (this.NSCount > 0) {
            System.out.println("Authoritive Section: ");
            for (Records record : additionalRecords){
            	record.outputRecord();
            }
        }

        
        if (this.ARCount > 0) {
            System.out.println("Additional Information Section:");
            for (Records record : additionalRecords){
            	record.outputRecord();
            }
        }
    }
    
    private void validateResponseQuestionType() {
        //Question starts at byte 13 (indexed at 11)
        int index = 12;

        while (this.response[index] != 0) {
            index++;
        }
        byte[] qType = {this.response[index + 1], this.response[index + 2]};

        if (qType[0] == 0) {
            if (qType[1] == 1) {
                queryType = "A";
            } 
        else if (qType[1] == 2) {
                queryType = "NS";
            }
        } else {
            System.out.println("INVALID QUESTION TYPE.");
        }
    }
    
    private void parseHeader(){
        //ID
        byte[] ID = new byte[2];
        ID[0] = response[0];
        ID[1] = response[1];
        this.ID = ID;
        //QR
        this.QR = getBit(response[2], 7) == 1;

        //AA
        this.AA = getBit(response[2], 2) == 1;

        //TC
        this.TC = getBit(response[2], 1) == 1;

        //RD
        this.RD = getBit(response[2], 0) == 1;

        //RA
        this.RA = getBit(response[3], 7) == 1;

        //RCODE
        this.RCode = response[3] & 0x0F;
        

        //QDCount
        byte[] QDCount = { response[4], response[5] };
        ByteBuffer wrapped = ByteBuffer.wrap(QDCount);
        this.QDCount = wrapped.getShort();
        
        //ANCount
        byte[] ANCount = { response[6], response[7] };
        wrapped = ByteBuffer.wrap(ANCount);
        this.ANCount = wrapped.getShort();
        
               
        //NSCount
        byte[] NSCount = { response[8], response[9] };
        wrapped = ByteBuffer.wrap(NSCount);
        this.NSCount = wrapped.getShort();
        
              
        //ARCount
        byte[] ARCount = { response[10], response[11] };
        wrapped = ByteBuffer.wrap(ARCount);
        this.ARCount = wrapped.getShort();
        
        
    }

     private int getBit(byte b, int position) {
    	return (b >> position) & 1;
    }
     private Records parseAnswer(int index){
    	Records result = new Records();
        
        String domain = "";
        int countByte = index;

        domain = getDomainFromIndex(countByte);
        countByte += getBytesFromDomain(domain);
        
        result.setNameServer(domain);

        //TYPE
        byte[] ans_type = new byte[2];
        ans_type[0] = response[countByte];
        ans_type[1] = response[countByte + 1];
        String type = "";
        if (ans_type[0] == 0) {
            if (ans_type[1] == 1) {
                type = "A";
            } else if (ans_type[1] == 2) {
                type = "NS";
            }
        } else {
            System.out.println("INVALID QUESTION TYPE.");
        }
        
        result.setQueryType(type);

        countByte += 8;
        
        //RDLength
        byte[] RDLength = { response[countByte], response[countByte + 1] };
        ByteBuffer wrapped = ByteBuffer.wrap(RDLength);
        int rdLength = wrapped.getShort();

        result.setRecordLength(rdLength);
        
        countByte +=2;
        switch (result.getQueryType()) {
            case "A":
                result.setDomain(parseATypeRDATA(rdLength, countByte));
                break;
            case "NS":
                result.setDomain(parseNSTypeRDATA(rdLength, countByte));
                break;
        }
        result.setByteLength(countByte + rdLength - index);
        return result;
    }
        private String parseATypeRDATA(int rdLength, int countByte) {
        String address = "";
        byte[] byteAddress= { response[countByte], response[countByte + 1], response[countByte + 2], response[countByte + 3] };
        try {
            InetAddress inetaddress = InetAddress.getByAddress(byteAddress);
            address = inetaddress.toString().substring(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
        
    }

    private String parseNSTypeRDATA(int rdLength, int countByte) {
		String nameServer = getDomainFromIndex(countByte);
    	
    	return nameServer;
    }
    
     private String getDomainFromIndex(int index){
    	int wordSize = response[index];
    	String domain = "";
    	boolean start = true;

    	while(wordSize != 0){
			if (!start){
				domain += ".";
			}
	    	if ((wordSize & 0xC0) == (int) 0xC0) {
	    		byte[] offset = { (byte) (response[index] & 0x3F), response[index + 1] };
	            ByteBuffer wrapped = ByteBuffer.wrap(offset);
	            domain += getDomainFromIndex(wrapped.getShort());
	            index += 2;
	            wordSize = 0;
	    	}else{
	    		domain += getWordFromIndex(index);
	    		index += wordSize + 1;
	    		wordSize = response[index];
	    	}
            start = false;
            
    	}
    	return domain;

    }
     
    private int getBytesFromDomain(String domain){
        int byteLength = 0;
		String[] items = domain.split("\\.");
		for(int i=0; i < items.length; i ++){
			// 1 byte length for the number value and then another for each character
			//www.mcgill.ca = 3, w, w, w, 6, m, c, g, i, l, l, 2, c, a = 14 byteLength
			byteLength += items[i].length() + 1;
		}
		return byteLength;
    }
    
    private String getWordFromIndex(int index){
    	String word = "";
    	int wordSize = response[index];
    	for(int i =0; i < wordSize; i++){
    		word += (char) response[index + i + 1];
		}
    	return word;
    }
    

}