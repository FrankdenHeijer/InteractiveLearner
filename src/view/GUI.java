package view;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Frank and Merijn on 20-12-16.
 */
public class GUI extends Frame implements ActionListener {

    private Label lbl;
    private Button btn;
    private TextField txt;
    
    /**
     *  GUI Constructor
     */
    public GUI() {
        setLayout(new FlowLayout());
        lbl = new Label("Interactive Learner");
        add(lbl);
        txt = new TextField("Interactive Learner");
        add(txt);
        btn = new Button("Test dataset");
        add(btn);
        setTitle("Frank and Merijn Interactive Learner");
        setVisible(true);
        setSize(500, 300);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String [] args) {
        GUI app = new GUI();
    }
}
