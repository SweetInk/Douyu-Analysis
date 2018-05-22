import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;

public class WindowMenuAction extends AnAction {


    public WindowMenuAction() {
        this.getTemplatePresentation().setIcon(IconLoader.getIcon("/asset/erha.jpeg"));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        String name = Messages.showInputDialog(
                "What is your name?", "Input Your Name",
                Messages.getQuestionIcon());

        System.out.println("edit test:" + name);
        this.getTemplatePresentation().setEnabled(false);
        // this.getTemplatePresentation().setEnabledAndVisible(false);
    }

    @Override
    public void update(AnActionEvent e) {
        if (!this.getTemplatePresentation().isEnabled()) {
            e.getPresentation().setEnabled(false);
        }

        super.update(e);
        System.out.println("UPDATE");
    }
}
