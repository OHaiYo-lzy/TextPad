import javax.swing.*;
import java.awt.*;

/**
 * @author Tyrion Lannister
 */
public class DialogFindAndReplace extends JDialog {

    protected static JPanel textPanel;
    protected static JPanel btnPanel;
    protected static JLabel findLabel;
    protected static JLabel replaceLabel;
    protected static JLabel statusLabel;
    protected static JTextField findText;
    protected static JTextField replaceText;
    protected static JButton prevBtn;
    protected static JButton nextBtn;
    protected static JButton replaceBtn;
    protected static JButton replaceAllBtn;

    public DialogFindAndReplace(Frame owner, String title){
        // 初始化域
        super(owner, title);
        setSize(270, 210);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);

        textPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        btnPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        findLabel = new JLabel("   查找：");
        replaceLabel = new JLabel("   替换：");
        findText = new JTextField();
        findText.setColumns(8);
        replaceText = new JTextField();
        prevBtn = new JButton("上一个");
        nextBtn = new JButton("下一个");
        replaceBtn = new JButton("替换");
        replaceAllBtn = new JButton("替换全部");
        statusLabel=new JLabel("                            ");

        // 对象部署
        prevBtn.setBackground(Color.white);
        nextBtn.setBackground(Color.white);
        replaceBtn.setBackground(Color.white);
        replaceAllBtn.setBackground(Color.white);

         //布局
        textPanel.add(findLabel);
        textPanel.add(findText);
        textPanel.add(replaceLabel);
        textPanel.add(replaceText);

        btnPanel.add(prevBtn);
        btnPanel.add(nextBtn);
        btnPanel.add(replaceBtn);
        btnPanel.add(replaceAllBtn);

        add(textPanel);
        add(btnPanel);
        add(statusLabel);

        // 事件
        prevBtn.addActionListener(e->findPrevious());
        nextBtn.addActionListener(e -> findNext(DialogFindAndReplace.findText.getText()));
        replaceBtn.addActionListener(e -> replace());
        replaceAllBtn.addActionListener(e -> replaceAll());
    }

    /**
     * 查找上一个方法
     */
    public void findPrevious() {
        // 查找的词
        String word = DialogFindAndReplace.findText.getText();
        // 当前光标焦点
        int focusPos = MainFrame.jta.getCaretPosition();
        // 文档内所有内容
        String fullText = MainFrame.jta.getText();
        int startPos = 0;
        int endPos = 0;
        boolean isfound = false;
        if ("".equals(word)) {
            JOptionPane.showMessageDialog(null, "【出错啦】",
                    "失败(请输入内容)", JOptionPane.ERROR_MESSAGE);
            return;
        }
        char lastLetter = word.charAt(word.length() - 1);
        for (int i = focusPos - 2; i > -1; i--) {
            if (fullText.charAt(i) == lastLetter) {
                isfound = true;
                for (int j = i - 1; j > i - word.length(); j--) {
                    if (fullText.charAt(j) != word.charAt(word.length() - i + j - 1)) {
                        isfound = false;
                        break;
                    }
                }
            }
            if (isfound) {
                startPos = i - word.length() + 1;
                endPos = i + 1;
                break;
            }
        }
        //显现查找得到的内容
        if (isfound) {
            MainFrame.jta.setSelectionStart(startPos);
            MainFrame.jta.setSelectionEnd(endPos);
            DialogFindAndReplace.statusLabel.setText("查找已执行!");
        }
        else {
            JOptionPane.showMessageDialog(null, "【出错啦】",
                    "失败(请输入内容)", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 查找下一个方法
     */
    public static boolean findNext(String str) {
        // 当前光标焦点
        int focousPos = MainFrame.jta.getCaretPosition();
        // 文档内所有内容
        String fullText = MainFrame.jta.getText();

        int startPos = 0;
        int endPos   = 0;
        boolean isfound = false;

        if ("".equals(str)) {
            JOptionPane.showMessageDialog(null, "【出错啦】",
                    "失败(请输入内容)", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        char firstLetter = str.charAt(0);
        for (int i = focousPos; i < fullText.length(); i++) {
            if (fullText.charAt(i) == firstLetter) {
                isfound = true;
                for (int j = i + 1; j < str.length() + i; j++) {
                    if (fullText.charAt(j) != str.charAt(j - i)) {
                        isfound = false;
                        break;
                    }
                }
            }
            if (isfound) {
                startPos = i;
                endPos = i + str.length();
                break;
            }
        }
        //显现查找得到的内容
        if (isfound) {
            MainFrame.jta.setSelectionStart(startPos);
            MainFrame.jta.setSelectionEnd(endPos);
            DialogFindAndReplace.statusLabel.setText("查找已执行!");
        }
        else {
            JOptionPane.showMessageDialog(null, "失败(找不到内容)",
                    "【出错啦】", JOptionPane.ERROR_MESSAGE);
        }
        return isfound;
    }

    /**
     * 替换当前方法
     */
    public static void replace() {
        String des = DialogFindAndReplace.replaceText.getText();
        String selectedTextext = MainFrame.jta.getSelectedText();
        if (selectedTextext != null) {
            MainFrame.jta.replaceSelection(des);
        }
        else {
            JOptionPane.showMessageDialog(null, "失败(没有选择内容)",
                    "【出错啦】", JOptionPane.ERROR_MESSAGE);        }
    }

    /**
     * 全部替换方法
     */
    public static void replaceAll() {
        boolean isReplaced = false;
        String from = DialogFindAndReplace.findText.getText();
        String to = DialogFindAndReplace.replaceText.getText();
        MainFrame.jta.setCaretPosition(0);
        while (findNext(from)) {
            MainFrame.jta.replaceSelection(to);
            isReplaced = true;
        }
        if (isReplaced) {
            DialogFindAndReplace.statusLabel.setText("替换成功");
        }
        else {
            JOptionPane.showMessageDialog(null, "失败(找不到内容)",
                    "【出错啦】", JOptionPane.ERROR_MESSAGE);        }
    }
}
