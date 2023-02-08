package com.julyyu.asplugins.tools.finduseless.ui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class DelectFileDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel textContent;
    private JLabel image;

    private File file;

    public DelectFileDialog(File file) {
        this.file = file;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("确定删除文件 -> ");
        stringBuilder.append(file.getName());
        textContent.setText(stringBuilder.toString());
        try {
            image.setIcon(new ImageIcon(file.getAbsolutePath()));
        }catch (Exception e){

        }
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setSize(600, 300);
        setLocationRelativeTo(null);
    }

    private void onOK() {
        try {
            if(file != null && file.exists()){
                file.delete();
            }
        }catch (Exception e){
            e.toString();
        }
        this.dispose();
    }

    private void onCancel() {
        this.dispose();
    }

    public static void showDeleteFileDialog(File file) {
        DelectFileDialog dialog = new DelectFileDialog(file);
//        dialog.pack();
        dialog.setVisible(true);
    }
}
