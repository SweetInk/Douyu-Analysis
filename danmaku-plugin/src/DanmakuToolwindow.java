import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.glass.ui.Size;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * create tool window
 * @author suchu
 * @since 2018/5/23 18:09
 */
public class DanmakuToolwindow implements ToolWindowFactory {
    private JPanel danmakuPanel;
    private Editor editor;
    private Timer timer;
    java.util.List<JLabel> d = new ArrayList<>();
    private static AffineTransform sAffineTransform = new AffineTransform();
    private static FontRenderContext sFontRenderContext = new FontRenderContext(sAffineTransform, true,
            true);
    Font font  = new Font("宋体",Font.BOLD,18);;
    Random random = new Random();


    void initD (){

        for(int i = 0;i<10;i++){
            addDanmu("我是弹幕啊啊 "+ i);
        }
    }

   public void addDanmu(String text){
        JLabel label = new JLabel();
        label.setFont(font);
        label.setText(text);
        int y = danmakuPanel.getHeight() / 20 * random.nextInt(20) + random.nextInt(32);
        Size stringSize = getStringSize(text, font);
        label.setSize(stringSize.width,stringSize.height);
        label.setLocation(danmakuPanel.getWidth(),y);
        d.add(label);
        danmakuPanel.add(label);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        DanmakuUIContainer container = new DanmakuUIContainer();
       editor =  FileEditorManager.getInstance(project).getSelectedTextEditor();
        container.setMyToolWindow(this);
        JLabel label = new JLabel();
        Font font  = new Font("宋体",Font.BOLD,64);;
        font = font.deriveFont(Font.BOLD, 32f);
        label.setFont(font);
        label.setText("hello ,this float label");
        danmakuPanel = initComboPanel();
        JComponent contentComponent = editor.getContentComponent();
        danmakuPanel.setSize(contentComponent.getWidth(),contentComponent.getHeight());
        label.setLocation(danmakuPanel.getWidth(),150);
        Size stringSize = getStringSize("hello ,this float label", font);
        label.setSize(stringSize.width,stringSize.height);
        initD();
        danmakuPanel.add(label, BorderLayout.CENTER);
        danmakuPanel.setLocation(0,0);
       // contentComponent.setLayout(new FlowLayout(FlowLayout.LEFT, (int) (x + contentComponent.getParent().getWidth() - danmakuPanel.getPreferredSize().getWidth() - 32), y + 32));
        contentComponent.add(danmakuPanel);
        timer = new Timer(25,e->{
            Iterator<JLabel> iterable = d.iterator();
            while(iterable.hasNext()){
                JLabel m = iterable.next();
                if(m.getX()+m.getWidth()<0){
                  iterable.remove();
                }else{
                    m.setLocation(m.getX()-3,m.getY());
                }

            }
        });
     //   toolWindow.getContentManager();
        JPanel panel = container.getPanel();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
       timer.start();
    }

    private JPanel initComboPanel() {
        JPanel panel = new JPanel();
        Color c = new Color(251,224,255,12);
        panel.setBackground(c);
        panel.setOpaque(false);//
        panel.setLayout(null);

        return panel;
    }

    public static Size getStringSize(String str, Font font) {
        if (str == null || str.length() == 0 || font == null) {
            return new Size(0, 0);
        }
        return new Size((int) font.getStringBounds(str, sFontRenderContext).getWidth(), (int) font.getStringBounds(str, sFontRenderContext).getHeight());
    }


}
