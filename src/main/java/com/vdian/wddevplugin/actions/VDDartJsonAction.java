package com.vdian.wddevplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.vdian.wddevplugin.tools.dartjsonformat.ui.DartJsonDialog;

public class VDDartJsonAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        // 目录文件右击进入New -> VDDartJsonFormat
        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile);

        DartJsonDialog jsonDialog = new DartJsonDialog(project, psiDirectory);
        jsonDialog.setVisible(true);

    }

}
