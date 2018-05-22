import client.DanmakuClient;
import client.MessageListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Toolwindow
 * @author suchu
 * @since 2018/5/22 15:59
 */
public class DanmakuToolWindow implements ToolWindowFactory, MessageListener {
    private JButton connectBtn;
    private ToolWindow myToolWindow;
    private JPanel test;
    private JLabel testLabel;
    private JTextArea outputTextArea;
    private JTextField txtRoomId;
    private JScrollPane scrollPanel;
    Executor executor;
    private volatile boolean started = false;
    private DanmakuClient client;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.myToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(test, "", false);
        toolWindow.getContentManager().addContent(content);
        executor = Executors.newFixedThreadPool(1);
        client = new DanmakuClient();
        client.setMessageListener(this);

    }


    public DanmakuToolWindow() {
        connectBtn.addActionListener((ActionEvent e) -> {
            String roomId = txtRoomId.getText();
            if (null == roomId && !"".equals(roomId)) {
                Messages.showInfoMessage("please input valid roomId", "warn");
            } else {
                connectBtn.setEnabled(false);
                if (!client.isConnected()) {
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
    public void init(ToolWindow window) {
    }

    @Override
    public void onChatMessage(String msg) {
        outputTextArea.append(msg + "\r\n");
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
