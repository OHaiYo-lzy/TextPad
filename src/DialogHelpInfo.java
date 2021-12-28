import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * @author Tyrion Lannister
 */
public class DialogHelpInfo extends JDialog {
    public DialogHelpInfo(Frame owner, String title) {
        super(owner, title);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);

        JPanel p= new JPanel();
        p.setBounds(0,0,getContentPane().getWidth(),getContentPane().getHeight());
        p.setLayout(null);
        p.setLayout(new FlowLayout());
        add(p);

        JTextArea l=new JTextArea();
        l.setBounds(0,0,getContentPane().getWidth(),getContentPane().getHeight());
        l.setFont(new Font("微软雅黑", Font.BOLD, 13));
        l.setText("TextPad的使用指南\n" +
                "进入TextPad，你可以选择打开已有文件或直接开始你的编辑\n" +
                "选择打开图标或者使用快捷键CTRL+O即可打开文件\n" +
                "\n在编辑文本的过程之中你还可以使用：\n" +
                "点击新建图标或使用快捷键CTRL+N来新建文件\n" +
                "点击撤销图标或使用快捷键CTRL+Z来撤销操作\n" +
                "点击恢复图标或使用快捷键CTRL+Y来恢复操作\n" +
                "点击保存图标或使用快捷键CTRL+S来保存文件\n" +
                "\n你还可以通过ALT+T进入设置\n" +
                "点击下拉框的选项来对文字进行字体和颜色的修改\n" +
                "\n当然你也可以通过点击菜单栏里面的文件，编辑等选项\n" +
                "通过不同的路径来实现以上的所有操作！\n" +
                "\n你还可以通过ALT+H进入帮助\n" +
                "以在不解或不知如何使用时获取帮助\n" +
                "\n相信聪明的你已经学会使用这个文本编辑器的最基本的功能啦\n" +
                "下面是作者刘哲源的github地址\n" +
                "如有更多的问题可以在TextPad项目中查看到源代码！\n");
        l.setEditable(false);
        l.setBorder(null);
        p.add(l);

        final JLabel myLink=new JLabel("进入刘哲源的github首页");
        myLink.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        myLink.setBounds(getContentPane().getWidth()/2,420,150,30);
        myLink.setForeground(Color.black);
        myLink.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("www.github.com/OHaiYo-lzy"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            //设置了当鼠标进行部件时候的动作
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                myLink.setBorder(new Border(){

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.white);
                        g.drawLine(0, height-1, width, height-1);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1,1,1,1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                myLink.setBorder(null);
            }

        });
        p.add(myLink);
    }
}
