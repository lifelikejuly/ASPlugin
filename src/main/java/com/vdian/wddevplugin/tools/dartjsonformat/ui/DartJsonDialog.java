package com.vdian.wddevplugin.tools.dartjsonformat.ui;

import com.google.gson.JsonElement;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.vdian.wddevplugin.tools.dartjsonformat.DartDataWriter;
import com.vdian.wddevplugin.tools.dartjsonformat.model.MustacheEntity;
import com.vdian.wddevplugin.utils.ClsUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

/**
 * @author shiki
 */
public class DartJsonDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton formatButton;
    private JEditorPane editorPane;
    private JLabel tip;
    private JTextField fileName;


    private PsiDirectory parentFolder;
    private Project project;
    private ClsUtils clsUtils;

    public DartJsonDialog(Project project, PsiDirectory parentFolder) {
        this.project = project;
        this.parentFolder = parentFolder;
        initView();
        clsUtils = new ClsUtils();
        initListener();
    }

    private void initView() {
        tip.setText("Generating code");
        tip.setVisible(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(600, 400);
        setLocationRelativeTo(null);
        editorPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hideTip();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hideTip();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hideTip();
            }
        });
    }

    private void hideTip() {
        if (this.tip.isVisible()) {
            this.tip.setVisible(false);
        }
    }

    private void initListener() {
        // 格式化
        formatButton.addActionListener(
                e -> {
                    String json = editorPane.getText().trim();
                    try {
                        // 文本内容
                        JsonElement jsonElement = clsUtils.tojsonElement(json);
                        editorPane.setText(clsUtils.toJsonText(jsonElement));
                    } catch (Exception exception) {
                        showTip("Json text format error");
                    }
                }
        );

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (fileName.getText() == null || "".equals(fileName.getText().trim())) {
//            Messages.showInfoMessage(project, "Please enter the File name", "Info");
            showTip("Please enter the File name");
            return;
        }
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                String jsonText = editorPane.getText().trim();
                String fileName = this.fileName.getText().trim();
                PsiFile dartFile = parentFolder.createFile(fileName + ".dart");
                MustacheEntity mustacheEntity = clsUtils.jsonElementToMustacheEntity(dartFile.getName(), jsonText);
                DartDataWriter dartDataWriter = new DartDataWriter(project, clsUtils, mustacheEntity, dartFile);
                dartDataWriter.start();
                dispose();
            } catch (Exception exception) {
                showTip("Json text format error");
            }
        });
    }

    private void onCancel() {
        dispose();
    }


    private void showTip(String tip) {
        if (!this.tip.isVisible()) {
            this.tip.setVisible(true);
        }
        this.tip.setText(tip);
    }

}
