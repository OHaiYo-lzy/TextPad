import java.awt.Font;
import javax.swing.JMenu;
/**
 * 自定义菜单
 * @author Tyrion Lannister
 */
public class MyMenu extends JMenu {

    public MyMenu(String text) {
        super(text);
        setBorder(null);
        setFont(new Font("微软雅黑", Font.PLAIN, 12));
    }
}
