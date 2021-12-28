
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author Tyrion Lannister
 */
public class DialogSettingFontStyle extends JDialog {

    private final int[] iFontStyle={Font.PLAIN,Font.BOLD,Font.ITALIC,Font.BOLD|Font.ITALIC};
    private final JComboBox nameComboBox;
    private final JComboBox sizeComboBox;
    private final JComboBox styleComboBox;
    private final JTextArea after;

    public DialogSettingFontStyle(Frame owner, String title){
        //初始化对话框
        super(owner, title);
        setSize(500, 300);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel jp=new JPanel();
        jp.setLayout(new FlowLayout());
        getContentPane().add("North",jp);

        JLabel nameLabel=new JLabel("字体:");
        //提取java字体库
        String[] fontList=GraphicsEnvironment.
                getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        nameComboBox=new JComboBox(fontList);
        //设置默认选择项
        nameComboBox.setSelectedItem("黑体");
        //部署更新after预览框的函数
        nameComboBox.addActionListener(e -> updateFont());
        jp.add(nameLabel);
        jp.add(nameComboBox);

        JLabel sizeLabel=new JLabel("大小");
        sizeComboBox =new JComboBox();
        //设置可改变的字体号
        for(int i=8;i<=18;i++){
            sizeComboBox.addItem(i+"");
        }
        sizeComboBox.addItem("36");
        sizeComboBox.addItem("48");
        sizeComboBox.addItem("72");
        //设置默认选择项
        sizeComboBox.setSelectedItem("14");
        //部署更新after预览框的函数
        sizeComboBox.addActionListener(e -> updateFont());
        jp.add(sizeLabel);
        jp.add(sizeComboBox);

        JLabel styleLabel=new JLabel("样式：");
        //提前设置可以改变的字体风格
        String[] strFontStyleName = {"普通", "粗体", "斜体", "粗斜体"};
        styleComboBox=new JComboBox(strFontStyleName);
        //设置默认选择项
        styleComboBox.setSelectedItem("粗体");
        //部署更新after预览框的函数
        styleComboBox.addActionListener(e -> updateFont());
        jp.add(styleLabel);
        jp.add(styleComboBox);

        JButton btnSetFont = new JButton("设置(S)");
        btnSetFont.setMnemonic('S');
        btnSetFont.addActionListener(e -> setFont());

        JButton btnColor = new JButton("颜色(C)");
        btnColor.setMnemonic('C');
        btnColor.addActionListener(e -> setColor());

        JButton btnExit = new JButton("退出(X)");
        btnExit.setMnemonic('X');
        btnExit.addActionListener(e -> System.exit(0));

        jp.add(btnSetFont);
        jp.add(btnColor);
        jp.add(btnExit);

        JPanel preview=new JPanel();
        preview.setLayout(new GridLayout(2,2,30,2));

        JLabel lblPreview =new JLabel("预览更改效果:");
        preview.add(lblPreview);

        JLabel zhanweizi =new JLabel();
        preview.add(zhanweizi);

        JTextArea before=new JTextArea();
        before.setText("这是一段示例文字\n展示了更改之前文本的模样\n" +
                    "This is a demo of \nthe style before the change");
        preview.add(before);

        after=new JTextArea();
        after.setText("这是一段示例文字\n展示了更改之后文本的模样\n" +
                "This is a demo of \nthe style after the change");
        preview.add(after);

        jp.add(preview);
        add(jp);
    }

    public void setFont(){
        String fontName=(String) nameComboBox.getSelectedItem();
        int size = Integer.parseInt((String) Objects.requireNonNull(sizeComboBox.getSelectedItem()));
        int index= styleComboBox.getSelectedIndex();
        int style=iFontStyle[index];
        Font font=new Font(fontName,style,size);
        MainFrame.jta.setFont(font);
    }

    public void updateFont(){
        String fontName=(String) nameComboBox.getSelectedItem();
        int size = Integer.parseInt((String) Objects.requireNonNull(sizeComboBox.getSelectedItem()));
        int index= styleComboBox.getSelectedIndex();
        int style=iFontStyle[index];
        Font font=new Font(fontName,style,size);
        after.setFont(font);
    }

    public void setColor(){
        Color color= JColorChooser.showDialog(this, "拾取颜色", Color.orange);
        MainFrame.jta.setForeground(color);
    }
}
