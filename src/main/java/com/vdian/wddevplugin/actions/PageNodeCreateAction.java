package com.vdian.wddevplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.vdian.wddevplugin.tools.samplenode.ui.NodeDialog;

public class PageNodeCreateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {

        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile);

        NodeDialog nodeDialog = new NodeDialog(project, psiDirectory);
        nodeDialog.setVisible(true);
    }
}
