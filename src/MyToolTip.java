import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JToolTip;
/**
 * 自定义的提示框类
 * @author Tyrion Lannister
 */
public class MyToolTip extends JToolTip{

    public MyToolTip() {
        super();
        super.setBorder(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getTipText().toCharArray().length * 15, 20);
    }

    @Override
    public void paintComponent(Graphics g) {
        //提示框底色为黑灰
        g.setColor(Color.darkGray);
        //设置矩形方位
        g.fillRect(0, 0, getWidth(), getHeight());
        //引用提示内容
        JLabel l = new JLabel(getTipText());
        //设置边界
        l.setBounds(3, 0, getWidth(), getHeight() - 2);
        //字色为白色
        l.setForeground(Color.white);
        //设置字体
        l.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        add(l);
    }
}
