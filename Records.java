/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.net.ServerSocket;

/**
 *
 * @author victorialariot
 */
public class Records {
    String server;
    String queryType;
    int recordLength;
    private int byteLength;
    
    public Records (String server, String queryType, int recordLength) {
        this.server = server;
        this.queryType = queryType;
        this.recordLength = recordLength;
    }
    
    public void outputRecord() {
        switch(this.queryType) {
            case "A":
                this.outputATypeRecords();
                break;
            case "NS":
                this.outputNSTypeRecords();
                break;
        }
    
    }
    
    private void outputATypeRecords() {
        System.out.println("IP\t" + this.domain + "\t" + this.timeToLive + "\t" + authString);
    }

    private void outputNSTypeRecords() {
        String authString = this.auth ? "auth" : "nonauth";
    	System.out.println("NS\t" + this.domain + "\t" + this.timeToLive + "\t" + authString);
    }
    
    public int getByteLength() {
        return byteLength;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
    
}
