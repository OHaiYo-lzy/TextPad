import java.awt.Font;
import javax.swing.JMenuItem;
/**
 * 自定义菜单元素
 * @author Tyrion Lannister
 */
public class MyMenuItem extends JMenuItem{

    public MyMenuItem(String text){
        super(text);
        setBorder(null);
        setFont(new Font("微软雅黑", Font.PLAIN, 12));
    }
}
