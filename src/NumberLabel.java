import java.awt.Font;
import javax.swing.JLabel;

/**
 * @author Tyrion Lannister
 */
public class NumberLabel extends JLabel{

    /**
     * 自定义的数字label , NumberPanel的组成单元
     * @param text 需展示的文本
     */
    public NumberLabel(String text){
        super(text);
        setFont(new Font("微软雅黑", Font.PLAIN, 12));
        setSize(27,20);
    }

}
