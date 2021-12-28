import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 * 自定义的数字条
 * @author Tyrion Lannister
 */
public class NumberPanel extends JPanel {
    int rows;
    int hsp=1;
    public NumberPanel() {
        super();
        setLayout(null);
        rows = MainFrame.lines;
        setSize(19, MainFrame.lines*19);
    }
    @Override
    public void paintComponent(Graphics g) {
        removeAll();
        rows = MainFrame.lines;
        setSize(19, rows*19);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 1; i <= rows; i++) {
            NumberLabel l = new NumberLabel(i + "");
            l.setLocation(2, 19 * (i - 1));
            add(l);
        }
    }
}
