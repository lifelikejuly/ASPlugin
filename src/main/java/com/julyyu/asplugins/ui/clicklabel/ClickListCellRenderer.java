package com.julyyu.asplugins.ui.clicklabel;

import com.julyyu.asplugins.tools.finduseless.OutPutInfo;

import javax.swing.*;
import java.awt.*;

public class ClickListCellRenderer extends JLabel implements ListCellRenderer {
    OutPutInfo outPutInfo;
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());



        outPutInfo = (OutPutInfo)value;
        setText(outPutInfo.getContent());




        setEnabled(list.isEnabled());
        setFont(list.getFont());

        return this;
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(getPreferredSize().width, getPreferredSize().height);
//    }
//
//
//    @Override
//    public void paint(Graphics g) {
//        g.fillRect(0,0,this.getWidth(),this.getHeight());
//        g.drawString(outPutInfo.getContent(),0,0);
//    }

}
