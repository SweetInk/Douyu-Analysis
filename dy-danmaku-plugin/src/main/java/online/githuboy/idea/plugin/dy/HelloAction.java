package online.githuboy.idea.plugin.dy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author suchu
 * @since 2019/4/24 17:04
 */
public class HelloAction extends AnAction {
    public HelloAction(){
        super("Hello");
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Messages.showMessageDialog(project,"this is very interesting hello,world hhh","Greeting",Messages.getInformationIcon());
    }
}
