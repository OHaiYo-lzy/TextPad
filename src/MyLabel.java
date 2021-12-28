import javax.swing.JLabel;
import javax.swing.JToolTip;
/**
 * 在自定义的label中生成提示框
 * @author Tyrion Lannister
 */
public class MyLabel extends JLabel {

    @Override
    public JToolTip createToolTip() {
        return new MyToolTip();
    }

}
