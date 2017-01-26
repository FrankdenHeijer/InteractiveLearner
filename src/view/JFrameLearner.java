/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

import controller.Protocol;
import controller.Learner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Classifier;


/**
 *
 * @author LENOVOW540
 */
public class JFrameLearner extends javax.swing.JFrame {

    private Classifier classifier;
    private Learner learner;
    public String fileLocation = "";
    public String classifiable = "";
    public Boolean predictionBoolean;
    public File fileUsed;
    private JFileChooser chooser;
    
    public Boolean getPredictionBoolean() {
       return predictionBoolean;
    }
    
    public String getClassifiable(){
        return classifiable;
    }

    /**
     * Creates new form JFrameLearner
     */
    public JFrameLearner() { initComponents(); }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public Learner getLearner() {

        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public void setTextarea(JTextArea textarea) {
        this.textarea = textarea;
    }

    public JTextArea getTextarea() {
        return textarea;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always

     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        textarea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();

        fileChooser.setDialogTitle("This is my open dialog");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textarea.setEditable(false);
        textarea.setColumns(20);
        textarea.setRows(5);
        jScrollPane1.setViewportView(textarea);

        jButton1.setText("Test File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Browse File");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextPane1.setEditable(false);
        jScrollPane2.setViewportView(jTextPane1);

        jButton2.setText("Train File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        Open.setText("Open");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenu1.add(Exit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Originated from NetBeans documentation.
    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        // TODO add your handling code here//
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select your file");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                File file = fileChooser.getSelectedFile();
            //try {
          // What to do with the file, e.g. display it in a TextArea
            File d = chooser.getCurrentDirectory();
            // We can use FileLocation to execute our methods regarding reading files etc. (instead of protocol)
            textarea.setEditable(false);
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );
            textarea.append("You succesfully added a directory to the learner");
            // catch (IOException ex) {
            System.out.println("problem accessing file"+d.getAbsolutePath());
            //}
    } else {
        System.out.println("File access cancelled by user.");
    }
 
    }//GEN-LAST:event_OpenActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_ExitActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        if(!fileLocation.equals("")) {
            classifiable = fileUsed.getParentFile().getName();
            learner.learn(classifier, this);
        }
        else{
        JOptionPane.showMessageDialog(null, "You should first select a file");}
        }catch(IOException e) {
            
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
               int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileUsed = fileChooser.getSelectedFile();
            //try {
          // What to do with the file, e.g. display it in a TextArea
            fileLocation = fileUsed.getAbsolutePath();
            System.out.println(fileLocation);
            // We can use FileLocation to execute our methods regarding reading files etc. (instead of protocol)
            textarea.setEditable(false);
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );
            //textarea.append("You succesfully added a file to the learner " + fileLocation);
            // catch (IOException ex) {
            jTextPane1.setText(fileLocation);
            //}
    } else {
        System.out.println("File access cancelled by user.");
    }              
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        classifiable = JOptionPane.showInputDialog("What class do you want to classify?", "");
    }//GEN-LAST:event_jButton2ActionPerformed

    public void predictionDialog(String prediction) throws IOException {
        String pString = JOptionPane.showInputDialog(prediction);
        System.out.println(pString);
        if(pString.equals("y")) {
            predictionBoolean = true;
            learner.continueLearning(classifier, this);
        }
        else{
            predictionBoolean = false;
        }
    }
    
    public void appendToTextarea(String string){
        textarea.append(string);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //try {
         //   for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         //       if ("Nimbus".equals(info.getName())) {
         //           javax.swing.UIManager.setLookAndFeel(info.getClassName());
         //           break;
         //       }
         //   }
        //    JFrame.setDefaultLookAndFeelDecorated(true);
        //} catch (ClassNotFoundException ex) {
        //    java.util.logging.Logger.getLogger(JFrameLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (InstantiationException ex) {
        //    java.util.logging.Logger.getLogger(JFrameLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (IllegalAccessException ex) {
        //    java.util.logging.Logger.getLogger(JFrameLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (javax.swing.UnsupportedLookAndFeelException ex) {
        //    java.util.logging.Logger.getLogger(JFrameLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFrameLearner().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Exit;
    private javax.swing.JMenuItem Open;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextArea textarea;
    // End of variables declaration//GEN-END:variables
}
