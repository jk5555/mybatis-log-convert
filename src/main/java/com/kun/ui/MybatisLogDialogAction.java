package com.kun.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * @author kun
 */
public class MybatisLogDialogAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        LogInputDialog logInputDialog = new LogInputDialog(project);
        logInputDialog.show();
    }
}
