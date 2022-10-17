package com.vdian.wddevplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.vdian.wddevplugin.tools.finduseless.ui.FindUselessDialog;

public class VDFindUselessAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        FindUselessDialog dialog = new FindUselessDialog();
        dialog.setVisible(true);
    }
}
