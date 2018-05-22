package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.*;

/**
 * @author suchu
 * @since 2018/5/18 17:00
 */
public class ClientTest {

    private static final String DANMAKU_HOST = "openbarrage.douyutv.com";
    private static final int port = 8601;

    public static void main(String[] args) throws IOException {

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //threadFactory.
        ExecutorService service = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), threadFactory);
        String host = "";
        Socket socket = new Socket(DANMAKU_HOST, port);
        RequestHandler requestHandler = new RequestHandler(socket);
        //set roomId

        requestHandler.setRoomId("606118");
        service.submit(requestHandler);
        service.submit(new KeepAlive(socket));
        //       new Thread(new KeepAlive(socket)).start();

    }

    public static void sendmsg(DataOutputStream dos, String content) throws IOException {
        if (null != dos) {
            byte[] datas = content.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(datas.length + 1);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            byte b = 0x00;
            buffer.put(datas);
            buffer.put((byte) '\0');
            int pack_Length = 8 + buffer.limit();

            ByteBuffer header = ByteBuffer.allocate(8);//fixed header length 8 byte.
            header.order(ByteOrder.LITTLE_ENDIAN);
            short magic = 689;
            // 4
            header.putInt(pack_Length);
            byte b1 = (byte) 0xb1;
            byte b2 = 0x02;
            header.put(b1);
            header.put(b2);
            header.put(b);
            header.put(b);


            // outputStream.write();
            // test
            ByteBuffer test = ByteBuffer.allocate(4);
            test.order(ByteOrder.LITTLE_ENDIAN);
            test.putInt(pack_Length);
            dos.write(test.array());
            dos.write(header.array());
            dos.write(buffer.array());
            dos.flush();
        }
    }

    static class RequestHandler implements Runnable {
        private Socket socket;
        boolean stop;
        private String roomId;

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                System.out.println("success connect to server ");
                //prepare login to danmaku server
                String srcData = "type@=loginreq/roomid@=" + roomId + "/";

                sendmsg(dos, srcData);
                while (!stop && !socket.isClosed()) {
                    if (dis.available() < 4) {
                        continue;
                    }
                    ByteBuffer temp = ByteBuffer.allocate(4);
                    temp.order(ByteOrder.LITTLE_ENDIAN);
                    byte[] tempArray = new byte[4];
                    dis.read(tempArray);
                    temp.put(tempArray);
                    temp.flip();//switch to read mode
                    int length = temp.getInt();
                    //      System.out.println("接受包长:" + length);
                    if (dis.available() < length) {
                        continue;
                    }
                    if (length < 0 || length < 8) {
                        continue;
                    }
                    ;
                    byte[] data = new byte[length];
                    int cur_len = dis.read(data);
                    ByteBuffer buffer1 = ByteBuffer.allocate(length);
                    buffer1.order(ByteOrder.LITTLE_ENDIAN);
                    buffer1.put(data);
                    buffer1.flip();//switch to read mode
          /*  System.out.println("length:" + buffer1.getInt());
            System.out.println("magic:" + buffer1.getShort());
            System.out.println("byte:" + buffer1.get());
            System.out.println("byte:" + buffer1.get());*/
                    byte[] msg = new byte[length - 8];
                    buffer1.position(8);
                    buffer1.get(msg);
                    Decode decode = new Decode();
                    String str = new String(msg);
                    str = str.substring(0, str.length() - 1);
                    decode.parse(str);
                    if (decode.getItem("type").equals("loginres")) {
                        //login res
                        System.out.println("received loginres.");
                        sendmsg(dos, "type@=joingroup/rid@=" + roomId + "/gid@=-9999/");
                    } else if (decode.getItem("type").equals("chatmsg")) {
                        System.out.println("【" + decode.getItem("nn") + "】:" + decode.getItem("txt"));
                    }else{
                        System.out.println(decode.sItems);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                stop = true;
                System.out.println("socket status:" + socket.isClosed());
                System.out.println("Socket has closed");
                if (null != socket) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    static class KeepAlive implements Runnable {

        private Socket socket;
        private volatile boolean stop = false;

        public KeepAlive(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            while (!stop) {
                try {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    sendmsg(dos, "type@=mrkl/");
                    Thread.sleep(45000);
                } catch (IOException e) {
                    e.printStackTrace();
                    stop = true;
                    System.out.println("socket status:" + socket.isClosed());
                    System.out.println("Socket has closed");
                    if (null != socket) {
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


