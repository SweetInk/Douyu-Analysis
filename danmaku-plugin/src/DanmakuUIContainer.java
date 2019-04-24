import client.DanmakuClient;
import client.MessageListener;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * DanmakuToolwindow UI component
 *
 * @author suchu
 * @since 2018/5/22 15:59
 */
public class DanmakuUIContainer implements MessageListener {
    private JButton connectBtn;
    private DanmakuToolwindow myToolWindow;
    private JPanel container;
    private JLabel testLabel;
    private JTextArea outputTextArea;
    private JTextField txtRoomId;
    private JScrollPane scrollPanel;
    private JLabel roomIdLabel;
    Executor executor;
    private volatile boolean started = false;
    private DanmakuClient client;


    public void setMyToolWindow(DanmakuToolwindow toolWindow) {
        this.myToolWindow = toolWindow;
    }

    public JPanel getPanel() {
        return this.container;
    }

    public DanmakuUIContainer() {
        executor = Executors.newFixedThreadPool(1);
        client = new DanmakuClient();
        client.setMessageListener(this);
        connectBtn.addActionListener((ActionEvent e) -> {
            String roomId = txtRoomId.getText();
            if (null == roomId || "".equals(roomId)) {
                Messages.showInfoMessage("please input valid roomId", "warn");
            } else {
                connectBtn.setEnabled(false);
                if (!client.isConnected()) {
                    outputTextArea.append("ready connect to server...\r\n");
                    //  connectBtn.setText("stop");
                    executor.execute(() -> {
                        client.setRoomId(roomId);
                        client.start();
                    });
                    connectBtn.setText("connecting...");
                } else {
                    // connectBtn.setText("connect");
                    client.close();
                    connectBtn.setText("connect");
                }
            }
        });
        testLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                outputTextArea.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }


    @Override
    public void onChatMessage(String msg) {
        outputTextArea.append(msg + "\r\n");
        myToolWindow.addDanmu(msg);
        outputTextArea.setCaretPosition(outputTextArea.getText().length());
        //scrollPanel.s
        //outputTextArea.scroll
    }

    @Override
    public void onGiftMessage(String msg) {
        outputTextArea.append(msg + "\r\n");
    }

    @Override
    public void onLoginRespMessage(String message) {
        outputTextArea.append(message + "\r\n");
    }

    @Override
    public void onConnected(String message) {
        outputTextArea.append("connect to server success\r\n");
        connectBtn.setEnabled(true);
        roomIdLabel.setText("current roomId:" + client.getRoomId());
        connectBtn.setText("stop");
    }

    @Override
    public void onConnectionClose(String message) {
        outputTextArea.append("connection has losted:" + message + "\r\n");
        connectBtn.setText("connect");
        connectBtn.setEnabled(true);
    }

    @Override
    public void onLoginError(String message) {
        outputTextArea.append(message + "\r\n");
    }
}
