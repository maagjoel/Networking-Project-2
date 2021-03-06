/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package mydns;

/**
 *
 * @author Victoria Lariot 6124058
 * @author Martin Alvarez 5856597
 * @author Gretel Gomez Rodriguez 6174028
 */
public class Records {

    String domain;
    String nameServer;
    String queryType;
    int recordLength;
    private int byteLength;

    public Records() {

    }

    public void outputRecord() {
        if (queryType.equals("A")) {
            this.outputATypeRecords();
        } else if (queryType.equals("NS")) {
            this.outputNSTypeRecords();
        }

    }

    private void outputATypeRecords() {
        System.out.println("Name: " + nameServer + "\tIP: " + domain);
    }

    private void outputNSTypeRecords() {
        System.out.println("Name: " + nameServer + "\tName Server: " + domain);
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

}
