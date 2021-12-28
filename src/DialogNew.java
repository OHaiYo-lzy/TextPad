import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 新建文件之前的对话框
 */
public class DialogNew extends JDialog {

    public DialogNew(Frame owner, String title) {
        super(owner, title);
        setSize(300, 150);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel info = new JLabel("当前文本已经修改， 是否保存?");
        info.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        info.setBounds(50,0,200,60);
        getContentPane().add(info);

        JButton yes=new JButton("是");
        yes.setBounds(60,70,60,25);
        yes.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        yes.addActionListener(e -> {
            DialogNew.this.dispose();
            MainFrame.window.save();
            MainFrame.window.edited=false;
            MainFrame.window.newFile();
            MainFrame.window.labelUndo.setEnabled(false);
            MainFrame.window.labelRedo.setEnabled(false);
            MainFrame.window.filepath=null;
        });
        getContentPane().add(yes);

        JButton no=new JButton("否");
        no.setBounds(150,70,60,25);
        no.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        no.addActionListener(e -> {
            DialogNew.this.dispose();
            MainFrame.window.edited=false;
            MainFrame.window.newFile();
            MainFrame.window.labelUndo.setEnabled(false);
            MainFrame.window.labelRedo.setEnabled(false);
            MainFrame.window.filepath=null;
        });
        getContentPane().add(no);
    }
}
