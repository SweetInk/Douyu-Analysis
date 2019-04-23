package online.githuboy.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author suchu
 * @since 2018/5/19 10:33
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8601);
        System.out.println("listening on port:" + 8601);

        Socket socket = serverSocket.accept();
        System.out.println("new connection has connected");

        InputStream inputStream = socket.getInputStream();
        DataInputStream dis = new DataInputStream(inputStream);
        int length = inputStream.available();
        System.out.println("available length:" + length);
         byte array[] = new byte[8*1024];
        // int len = inputStream.read(array);
        //  System.out.println(len);

        while (socket.isConnected()){
            if(dis.available()<4) {
                continue;
            }
            ByteBuffer b1 = ByteBuffer.allocate(4);
            b1.order(ByteOrder.LITTLE_ENDIAN);
            byte [] len = new byte[4];
            dis.read(len);
            b1.put(len);
            b1.flip();
            int h_l = b1.getInt();
            System.out.println("length:" + h_l);
            if(dis.available()<h_l) {
                continue;
            }
            ByteBuffer left = ByteBuffer.allocate(h_l);
            left.order(ByteOrder.LITTLE_ENDIAN);
            byte []l_b = new byte[h_l+1];
          int cur_length =   dis.read(l_b);

            left.put(l_b,0,cur_length-1);
            left.flip();
            System.out.println("length:" + left.getInt());
            System.out.println("magic:" + left.getShort());
            System.out.println("byte:" + left.get());
            System.out.println("byte:" + left.get());
            byte buffer[] = new byte[h_l - 8];
           left.get(buffer);
            System.out.println(new String(buffer));
        }
     //   dis.read(array);

        //String line = br.readLine();
        //  System.out.println(line);
    /*    while (len != -1) {
      //      System.out.println(line);
            len = inputStream.read(array);
            System.out.println("len:"+len);
        }*/
    }
}
