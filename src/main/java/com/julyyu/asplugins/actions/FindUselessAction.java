package com.julyyu.asplugins.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.julyyu.asplugins.tools.finduseless.ui.FindUselessDialog;

public class FindUselessAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        FindUselessDialog dialog = new FindUselessDialog();
        dialog.setVisible(true);
    }
}
