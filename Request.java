//package mydns;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 *
 * @author Victoria Lariot 6124058
 * @author Martin Alvarez 5856597
 * @author Gretel Gomez Rodriguez 6174028
 */
public class Request {

    private String domain;
    private String type;

    public Request(String domain, String type) {
        this.domain = domain;
        this.type = type;
    }

    public byte[] getRequest() {
        int qNameLength = domain.length() + 1;
        ByteBuffer request = ByteBuffer.allocate(12 + 5 + qNameLength);
        request.put(createRequestHeader());
        request.put(createQuestionHeader(qNameLength));
        return request.array();
    }

    private byte[] createRequestHeader() {
        ByteBuffer header = ByteBuffer.allocate(12);

        header.put((byte) 0x13);
        header.put((byte) 0x50);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x01);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x00);
        header.put((byte) 0x00);

        return header.array();
    }

    private byte[] createQuestionHeader(int qNameLength) {
        ByteBuffer question = ByteBuffer.allocate(qNameLength + 5);

        String[] items = domain.split("\\.");
        for (int i = 0; i < items.length; i++) {
            question.put((byte) items[i].length());
            for (int j = 0; j < items[i].length(); j++) {
                question.put((byte) ((int) items[i].charAt(j)));

            }
        }
        question.put((byte) 0x00);

        //Add Query Type
        question.put((byte) 0x00);
        question.put((byte) 0x01);

        //Add Query Class
        question.put((byte) 0x00);
        question.put((byte) 0x01);

        return question.array();
    }
}
