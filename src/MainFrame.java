import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.undo.UndoManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tyrion Lannister
 */
public class MainFrame {
    /**
     * 初始化
     */
    protected static MainFrame  window;
    protected static JTextArea  jta;
    protected JFrame            frmTextPad;
    private   JPanel            panel;
    private   NumberPanel       np;
    private   MyMenuItem        menuItemSave;
    private   MyMenuItem        menuItemUndo;
    private   MyMenuItem        menuItemRedo;
    private   MyMenuItem        menuItemCut;
    private   MyMenuItem        menuItemCopy;
    private   MyMenuItem        menuItemPaste;
    private   MyMenuItem        menuItemDelete;
    private   MyMenuItem        myCut;
    private   MyMenuItem        myCopy;
    private   MyMenuItem        myPaste;
    private   MyMenuItem        myDelete;
    private   UndoManager       um;
    protected JLabel            labelSave;
    protected JLabel            labelUndo;
    protected JLabel            labelRedo;
    protected JPopupMenu        pop                 = new JPopupMenu();
    protected String            filepath            = null;
    static final int            MAXIMUM_LINES       = 24;
    static final int            NUMBER_LABEL_HEIGHT = 17;
    static final int            TEXT_AREA_HEIGHT    = 412;
    boolean                     edited              = false;
    static int                  lines               = 1;

    /**
     * 启动TextPad
     */
    public static void main(String[] args) {
            window = new MainFrame();
            window.frmTextPad.setVisible(true);
    }

    /**
     * 创建TextPad
     */
    public MainFrame() {
        //初始化frame内容
        initializeFrame();
        //初始化菜单栏
        initializeMenu();
        //初始化工具条内容
        initializeToolBar();
        //初始化PopUpMenu
        initializePopUpMenu();
    }

    /**
     * 初始化frame内容，包括frm的综合设置，jta的属性，滚动条和数字版的安排
     */
    private void initializeFrame() {

        frmTextPad = new JFrame();
        //设置文本编辑器标题
        frmTextPad.setTitle("TextPad");
        //设置默认字体
        frmTextPad.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        //设置边界
        frmTextPad.setBounds(100, 100, 635, 506);
        //设置关闭状态，实质上已经由其他函数完成了关闭前需要做的动作
        frmTextPad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //设置布局
        frmTextPad.getContentPane().setLayout(null);
        //默认设置窗口不可改变大小
        frmTextPad.setResizable(false);
        //设置位置不关联，窗口将置于屏幕的中央。
        frmTextPad.setLocationRelativeTo(null);
        //设置主图标icon
        frmTextPad.setIconImage(new ImageIcon(Objects.requireNonNull(MainFrame.class
                .getResource("/img/textPad.png"))).getImage());
        //监听
        frmTextPad.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!edited) {
                    System.exit(0);
                }
                else {
                    new DialogExit(frmTextPad, "Save");
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

        });

        jta = new JTextArea();
        um = new UndoManager();

        jta.getDocument().addUndoableEditListener(um);
        jta.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        jta.setLineWrap(false);
        jta.setOpaque(true);

        //提供文件拖拽打开的功能
        jta.setTransferHandler(new TransferHandler(){
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();

                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }

                    StringBuilder context = new StringBuilder();
                    //外部try-catch来检验文件是否打开成功
                    try {
                        FileReader fr = new FileReader(filepath);
                        BufferedReader br = new BufferedReader(fr);
                        String message;
                        //内嵌套try-catch来检验读取文本内容时候是否出错
                        try {
                            while ((message = br.readLine()) != null) {

                                context.append(message).append("\n");
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    jta.setText("已读入文件:\n"+filepath+"\n\n"+context);

                    return true;
                }
                catch (Exception ignored) {

                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (DataFlavor flavor : flavors) {
                    if (DataFlavor.javaFileListFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }

        });

        //键盘时间监听
        jta.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!e.isActionKey()) {
                    int []noprint = {KeyEvent.VK_UNDEFINED, KeyEvent.VK_SHIFT,
                                    KeyEvent.VK_CONTROL, KeyEvent.VK_ALT,
                                    KeyEvent.VK_PAUSE, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_ESCAPE,
                                    KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN,
                                    KeyEvent.VK_END, KeyEvent.VK_HOME,
                                    KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,
                                    KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                                    KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3,
                                    KeyEvent.VK_F4, KeyEvent.VK_F5, KeyEvent.VK_F6,
                                    KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9,
                                    KeyEvent.VK_F10, KeyEvent.VK_F11,KeyEvent.VK_F12,
                                    KeyEvent.VK_DELETE,KeyEvent.VK_NUM_LOCK, KeyEvent.VK_INSERT};
                    int code = e.getKeyCode();
                    AtomicBoolean result = new AtomicBoolean(false);
                    //如果键盘输入了以上的键之一，则将设置为true
                    for (int i = 0; i <= noprint.length - 1; i++) {
                        if (code == noprint[i]) {
                            result.set(true);
                            break;
                        }
                    }
                    //当键盘的输入不为以上的键时候
                    if (!result.get()) {
                        //设置保存相关的部件的状态
                        edited = true;
                        menuItemSave.setEnabled(true);
                        labelSave.setEnabled(true);

                        if ("Enter".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                            lines = jta.getLineCount() + 1;
                        }
                        else if ("Backspace".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                            lines = Math.max(jta.getLineCount() - 1, 1);
                        }
                        //检测总行数是否炒股界面可以显示的最多的行数
                        if (lines > MAXIMUM_LINES) {
                            np.hsp = (lines - MAXIMUM_LINES) * NUMBER_LABEL_HEIGHT;
                            np.setLocation(1, np.hsp);
                        }
                        else {
                            np.hsp = 1;
                            np.setLocation(1, 1);
                        }
                        panel.repaint();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //释放键盘之后设置撤销和恢复的状态
                if (um.canUndo()) {
                    menuItemUndo.setEnabled(true);
                    labelUndo.setEnabled(true);
                }
                else {
                    menuItemUndo.setEnabled(false);
                    labelUndo.setEnabled(false);
                }
                if (um.canRedo()) {
                    menuItemRedo.setEnabled(true);
                    labelRedo.setEnabled(true);
                }
                else {
                    menuItemRedo.setEnabled(false);
                    labelRedo.setEnabled(false);
                }
            }

        });
        //鼠标事件监听
        jta.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (jta.getSelectedText() != null && !"".equals(jta.getSelectedText())) {
                        myCut.setEnabled(true);
                        myCopy.setEnabled(true);
                        myDelete.setEnabled(true);
                    }
                    else {
                        myCut.setEnabled(false);
                        myCopy.setEnabled(false);
                        myDelete.setEnabled(false);
                    }
                    //检查剪贴板状态，如果不为空则设置复制部件为可用状态
                    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable t = cb.getContents(null);
                    if (t != null) {
                        myPaste.setEnabled(t.isDataFlavorSupported(DataFlavor.stringFlavor));
                    }
                    else {
                        myPaste.setEnabled(false);
                    }
                    //显示右键菜单
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        //滚动版设置
        JScrollPane scrollPane = new JScrollPane(jta);
        scrollPane.setBounds(20, 59, 609, 413);
        //鼠标滚动事件监听lambda
        scrollPane.addMouseWheelListener(e -> {
            int count = e.getWheelRotation();
            if (count > 0) {
                if (lines > MAXIMUM_LINES && TEXT_AREA_HEIGHT - np.getY() + NUMBER_LABEL_HEIGHT <= np.getHeight()) {
                    np.setLocation(1, np.getY() - NUMBER_LABEL_HEIGHT);
                }
            }
            else {
                if (lines > MAXIMUM_LINES && np.getY() + NUMBER_LABEL_HEIGHT <= 0) {
                    np.setLocation(1, np.getY() + NUMBER_LABEL_HEIGHT);
                }
            }

        });
        frmTextPad.getContentPane().add(scrollPane);

        //垂直滚动条设置
        JScrollBar jsb = scrollPane.getVerticalScrollBar();
        jsb.addAdjustmentListener( e -> {
                if (lines > MAXIMUM_LINES) {
                    np.setLocation(1, -e.getValue());
                    panel.repaint();
                }
        });

        //数字行数板块
        panel = new JPanel();
        panel.setBounds(0, 59, 20, TEXT_AREA_HEIGHT);
        panel.setBackground(Color.lightGray);
        //设置数字版的边框属性
        panel.setBorder(new Border() {

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.gray);
                g.drawRect(0, 0, width, height - 1);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(1, 1, 1, 1);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }

        });
        np = new NumberPanel();
        np.setLocation(1, 1);
        panel.setLayout(null);
        panel.add(np);
        frmTextPad.getContentPane().add(panel);

    }

    /**
     * 初始化菜单栏
     */
    private void initializeMenu(){
        JMenuBar menuBar = new JMenuBar();
        //设置位置信息
        menuBar.setBounds(0, 0, 629, 29);
        //主frame添加菜单栏
        frmTextPad.getContentPane().add(menuBar);

        //文件块
        JMenu menuFile = new MyMenu("文件(F)");
        //设置快速键ALT+F
        menuFile.setMnemonic('F');
        menuBar.add(menuFile);

        JMenuItem menuItem = new MyMenuItem("新建");
        //lambda
        menuItem.addActionListener(e -> newFile());
        //设置快捷键CTRL+N
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        menuFile.add(menuItem);

        //打开块
        JMenuItem menuItemOpen = new MyMenuItem("打开");
        //lambda
        menuItemOpen.addActionListener(e -> open());
        //设置快捷键CTRL+O
        menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        menuFile.add(menuItemOpen);

        //保存模块
        menuItemSave = new MyMenuItem("保存");
        //lambda
        menuItemSave.addActionListener(e -> save());
        //设置默认不可用
        menuItemSave.setEnabled(false);
        //设置快捷键CTRL+S
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(menuItemSave);

        //另存为模块
        MyMenuItem menuItemSaveToOtherPath = new MyMenuItem("另存为");
        //lambda
        menuItemSaveToOtherPath.addActionListener(e -> saveToAnotherPath());
        //快捷键CTRL+ALT+S
        menuItemSaveToOtherPath.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        menuFile.add(menuItemSaveToOtherPath);

        //退出
        JMenuItem menuItemExit = new MyMenuItem("退出");
        //lambda
        menuItemExit.addActionListener(e -> {
            if (!edited) {
                System.exit(0);
            }
            else {
                new DialogExit(frmTextPad, "Save");
            }
        });
        menuFile.add(menuItemExit);

        //编辑
        JMenu menuEdit = new MyMenu("编辑(E)");
        //设置编辑栏里面选项的可用状态
        menuEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //如果有选中文本，则将剪切，复制，删除的Item设为可用状态,否则设置为不可用
                if (jta.getSelectedText() != null && !"".equals(jta.getSelectedText())) {
                    menuItemCut.setEnabled(true);
                    menuItemCopy.setEnabled(true);
                    menuItemDelete.setEnabled(true);
                }
                else {
                    menuItemCut.setEnabled(false);
                    menuItemCopy.setEnabled(false);
                    menuItemDelete.setEnabled(false);
                }
                //检查系统的粘贴板的状态，如果有内容则设置可用状态，否则设置为不可用
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(null);
                if (t != null) {
                    menuItemPaste.setEnabled(t.isDataFlavorSupported(DataFlavor.stringFlavor));
                }
                else {
                    menuItemPaste.setEnabled(false);
                }
            }

        });
        //快速键
        menuEdit.setMnemonic('E');
        menuBar.add(menuEdit);

        //撤销
        menuItemUndo = new MyMenuItem("撤销");
        menuItemUndo.addActionListener(e -> {
            if (um.canUndo()) {
                um.undo();
            }
            //设置撤销和恢复建的可用状态，工具条上的和编辑栏里面的都要设置
            if (um.canUndo()) {
                menuItemUndo.setEnabled(true);
                labelUndo.setEnabled(true);
            }
            else {
                menuItemUndo.setEnabled(false);
                labelUndo.setEnabled(false);
            }
            if (um.canRedo()) {
                menuItemRedo.setEnabled(true);
                labelRedo.setEnabled(true);
            }
            else {
                menuItemRedo.setEnabled(false);
                labelRedo.setEnabled(false);
            }
        });
        //设置默认不可用
        menuItemUndo.setEnabled(false);
        //设置快捷键CTRL+Z
        menuItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemUndo);

        //恢复，同理撤销
        menuItemRedo = new MyMenuItem("恢复");
        menuItemRedo.addActionListener(e -> {
            if (um.canRedo()) {
                um.redo();
            }
            if (um.canUndo()) {
                menuItemUndo.setEnabled(true);
                labelUndo.setEnabled(true);
            }
            else {
                menuItemUndo.setEnabled(false);
                labelUndo.setEnabled(false);
            }
            if (um.canRedo()) {
                menuItemRedo.setEnabled(true);
                labelRedo.setEnabled(true);
            }
            else {
                menuItemRedo.setEnabled(false);
                labelRedo.setEnabled(false);
            }
        });
        //设置默认不可用
        menuItemRedo.setEnabled(false);
        //设置快捷键CTRL+Y
        menuItemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemRedo);

        //剪切
        menuItemCut = new MyMenuItem("剪切");
        menuItemCut.addActionListener(e -> cut());
        menuItemCut.setEnabled(false);
        menuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemCut);

        //复制
        menuItemCopy = new MyMenuItem("复制");
        menuItemCopy.addActionListener(e -> copy());
        menuItemCopy.setEnabled(false);
        menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemCopy);

        //粘贴
        menuItemPaste = new MyMenuItem("粘贴");
        menuItemPaste.addActionListener(e -> paste());
        menuItemPaste.setEnabled(false);
        menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemPaste);

        //删除
        menuItemDelete = new MyMenuItem("删除");
        menuItemDelete.addActionListener(e -> delete());
        menuItemDelete.setEnabled(false);
        menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuEdit.add(menuItemDelete);

        //全选
        MyMenuItem menuItemSelectAll = new MyMenuItem("全选");
        menuItemSelectAll.addActionListener(e -> selectAll());
        menuItemSelectAll.setEnabled(true);
        menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemSelectAll);

        //从文件添加文本
        MyMenuItem menuItemAppendTextFromFile=new MyMenuItem("从文件添加文本");
        menuItemAppendTextFromFile.addActionListener(e -> appendTextFromFile());
        menuItemAppendTextFromFile.setEnabled(true);
        menuEdit.add(menuItemAppendTextFromFile);

        //时间，日期
        MyMenuItem menuItemTime=new MyMenuItem("时间/日期(D)");
        menuItemTime.addActionListener(e -> addTime());
        menuItemTime.setEnabled(true);
        menuItemTime.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        menuEdit.add(menuItemTime);


        //查找与替换
        JMenu menuFindAndReplace =new MyMenu("查找与替换(R)");
        menuFindAndReplace.setMnemonic('R');
        menuBar.add(menuFindAndReplace);

        JMenuItem menuItemFindAndReplace = new MyMenuItem("查找与替换");
        menuItemFindAndReplace.addActionListener(e->new DialogFindAndReplace(frmTextPad,"查找与替换"));
        menuItemFindAndReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        menuFindAndReplace.add(menuItemFindAndReplace);


        //打印
        JMenu menuPrint =new MyMenu("页面(P)");
        menuPrint.setMnemonic('P');
        menuBar.add(menuPrint);

        //统计
        JMenuItem menuItemStatistics=new MyMenuItem("统计信息");
        menuItemStatistics.addActionListener(e -> statistics());
        menuItemStatistics.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        menuPrint.add(menuItemStatistics);

        JMenuItem menuItemPrint=new MyMenuItem("打印文本");
        menuItemPrint.addActionListener(e -> new DialogPrint());
        menuItemPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        menuPrint.add(menuItemPrint);


        //设置
        JMenu menuSetting = new MyMenu("设置(T)");
        menuSetting.setMnemonic('T');
        menuBar.add(menuSetting);

        //主题
        JMenu menuItemTheme=new JMenu("主题");

        //主题选项
        JMenuItem themeOrg=new MyMenuItem("原始主题");
        JMenuItem themeDark=new MyMenuItem("深色主题");
        JMenuItem themeProtect=new MyMenuItem("护眼");
        JMenuItem themeOcean=new MyMenuItem("海洋");
        JMenuItem themePopular=new MyMenuItem("2022流行色");
        JMenuItem themeWild=new MyMenuItem("羊皮纸");

        menuItemTheme.add(themeOrg);
        menuItemTheme.add(themeDark);
        menuItemTheme.add(themeProtect);
        menuItemTheme.add(themeOcean);
        menuItemTheme.add(themePopular);
        menuItemTheme.add(themeWild);

        themeOrg.addActionListener(e -> jta.setBackground(new Color(255, 255, 255)));
        themeDark.addActionListener(e -> jta.setBackground(new Color(170, 176, 182)));
        themeProtect.addActionListener(e -> jta.setBackground(new Color(204,232,207)));
        themeOcean.addActionListener(e -> jta.setBackground(new Color(40,150,200)));
        themePopular.addActionListener(e -> jta.setBackground(new Color(104, 102, 171)));
        themeWild.addActionListener(e -> jta.setBackground(new Color(242,235,217)));

        menuSetting.add(menuItemTheme);

        JMenuItem menuItemSettingStyle=new MyMenuItem("设置字体样式");
        menuItemSettingStyle.addActionListener(e ->new DialogSettingFontStyle(frmTextPad,"设置字体样式"));
        menuSetting.add(menuItemSettingStyle);


        //帮助
        JMenu menuHelp=new MyMenu("帮助(H)");
        menuHelp.setMnemonic('H');
        menuBar.add(menuHelp);

        JMenuItem menuItemHelp =new MyMenuItem("获取帮助");
        menuItemHelp.addActionListener(e -> new DialogHelpInfo(frmTextPad,"Help"));
        menuHelp.add(menuItemHelp);

        JMenuItem menuItemAbout =new MyMenuItem("关于TextPad");
        menuItemAbout.addActionListener(e -> JOptionPane.showMessageDialog
                (frmTextPad, "TextPad是刘哲源的java课程设计作品\n如需反馈问题，请移步我的github网站\nwww.github.com/OHaiYo-lzy", "关于TextPad", JOptionPane.PLAIN_MESSAGE));
        menuHelp.add(menuItemAbout);

    }

    /**
     * 初始化工具栏
     */
    private void initializeToolBar(){
        //新建 设置标签，提示，监听，边界和图标
        final JLabel lblNewLabel = new MyLabel();
        lblNewLabel.setToolTipText("新建");
        lblNewLabel.addMouseListener(new MouseAdapter() {

            @Override
            //鼠标进入加载tool tip 各类属性
            public void mouseEntered(MouseEvent e) {
                lblNewLabel.setBorder(new Border() {

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.gray);
                        g.drawRoundRect(0, 0, width - 1, height - 1, 3, 3);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1, 1, 1, 1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }

                });

                frmTextPad.setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            //鼠标移出则不显示
            public void mouseExited(MouseEvent e) {
                lblNewLabel.setBorder(null);
                frmTextPad.setCursor(Cursor.DEFAULT_CURSOR);
            }

            //鼠标点击释放则新建文件
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newFile();
                }
            }

        });
        lblNewLabel.setBounds(10, 32, 25, 25);
        lblNewLabel.setIcon(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/new.png"))));
        frmTextPad.getContentPane().add(lblNewLabel);

        //同理 设置标签，提示，监听，边界和图标
        final JLabel labelOpen = new MyLabel();
        labelOpen.setToolTipText("打开");
        labelOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelOpen.setBorder(new Border() {

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.gray);
                        g.drawRoundRect(0, 0, width - 1, height - 1, 3, 3);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1, 1, 1, 1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }

                });
                frmTextPad.setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelOpen.setBorder(null);
                frmTextPad.setCursor(Cursor.DEFAULT_CURSOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    open();
                }
            }
        });
        labelOpen.setBounds(40, 32, 25, 25);
        labelOpen.setIcon(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/open.png"))));
        frmTextPad.getContentPane().add(labelOpen);

        //同理 设置标签，提示，监听，边界和图标，默认初始化不可用
        labelSave = new MyLabel();
        labelSave.setEnabled(false);
        labelSave.setToolTipText("保存");
        labelSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelSave.setBorder(new Border() {

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.gray);
                        g.drawRoundRect(0, 0, width - 1, height - 1, 3, 3);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1, 1, 1, 1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }

                });
                if (labelRedo.isEnabled()){
                    frmTextPad.setCursor(Cursor.HAND_CURSOR);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSave.setBorder(null);
                frmTextPad.setCursor(Cursor.DEFAULT_CURSOR);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (labelSave.isEnabled())
                    {
                        save();
                    }
                }
            }
        });

        labelSave.setBounds(70, 32, 25, 25);
        labelSave.setIcon(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/save.png"))));
        frmTextPad.getContentPane().add(labelSave);

        //同理 设置标签，提示，监听，边界和图标，默认初始化不可用
        labelUndo = new MyLabel();
        labelUndo.setEnabled(false);
        labelUndo.setToolTipText("撤销");
        labelUndo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelUndo.setBorder(new Border() {

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.gray);
                        g.drawRoundRect(0, 0, width - 1, height - 1, 3, 3);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1, 1, 1, 1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }

                });
                if (labelUndo.isEnabled()){
                    frmTextPad.setCursor(Cursor.HAND_CURSOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelUndo.setBorder(null);
                frmTextPad.setCursor(Cursor.DEFAULT_CURSOR);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (labelUndo.isEnabled()) {
                        if (um.canUndo()) {
                            um.undo();
                        }
                        //设置撤销和恢复状态
                        if (um.canUndo()) {
                            menuItemUndo.setEnabled(true);
                            labelUndo.setEnabled(true);
                        }
                        else {
                            menuItemUndo.setEnabled(false);
                            labelUndo.setEnabled(false);
                        }
                        if (um.canRedo()) {
                            menuItemRedo.setEnabled(true);
                            labelRedo.setEnabled(true);
                        }
                        else {
                            menuItemRedo.setEnabled(false);
                            labelRedo.setEnabled(false);
                        }
                    }
                }
            }
        });

        labelUndo.setBounds(100, 32, 25, 25);
        labelUndo.setIcon(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/undo.png"))));
        frmTextPad.getContentPane().add(labelUndo);

        //恢复 设置标签，提示，监听，边界和图标，默认初始化不可用
        labelRedo = new MyLabel();
        labelRedo.setEnabled(false);
        labelRedo.setToolTipText("恢复");
        labelRedo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelRedo.setBorder(new Border() {

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        g.setColor(Color.gray);
                        g.drawRoundRect(0, 0, width - 1, height - 1, 3, 3);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(1, 1, 1, 1);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return true;
                    }

                });
                if (labelRedo.isEnabled()){
                    frmTextPad.setCursor(Cursor.HAND_CURSOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelRedo.setBorder(null);
                frmTextPad.setCursor(Cursor.DEFAULT_CURSOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (labelRedo.isEnabled()) {
                        if (um.canRedo()) {
                            um.redo();
                        }
                        //设置撤销和恢复状态
                        if (um.canUndo()) {
                            menuItemUndo.setEnabled(true);
                            labelUndo.setEnabled(true);
                        }
                        else {
                            menuItemUndo.setEnabled(false);
                            labelUndo.setEnabled(false);
                        }
                        if (um.canRedo()) {
                            menuItemRedo.setEnabled(true);
                            labelRedo.setEnabled(true);
                        }
                        else {
                            menuItemRedo.setEnabled(false);
                            labelRedo.setEnabled(false);
                        }
                    }
                }
            }
        });
        labelRedo.setBounds(130, 32, 25, 25);
        labelRedo.setIcon(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/redo.png"))));
        frmTextPad.getContentPane().add(labelRedo);
    }

    /**
     * 初始化PopUpMenu
     */
    private void initializePopUpMenu(){
        //cut剪切模块
        myCut = new MyMenuItem("剪切");
        //快捷键为 CTRL+X
        myCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        //监听重载
        myCut.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    cut();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        //copy复制模块
        myCopy = new MyMenuItem("复制     ");
        myCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        //监听重载
        myCopy.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    copy();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        //粘贴模块
        myPaste = new MyMenuItem("粘贴     ");
        //快捷键为 CTRL+V
        myPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        //监听重载
        myPaste.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    paste();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        //删除模块
        myDelete = new MyMenuItem("删除     ");
        //快捷键为 DELETE
        myDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        //监听重载
        myDelete.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    delete();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        //全选模块
        MyMenuItem selectAll = new MyMenuItem("全选     ");
        //快捷键为 CTRL+A
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        //监听重载
        selectAll.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    selectAll();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });

        //默认设置cut,copy,paste,delete,select_all为不可用状态
        myCut.setEnabled(false);
        myCopy.setEnabled(false);
        myPaste.setEnabled(false);
        myDelete.setEnabled(false);
        selectAll.setEnabled(true);

        //在弹出菜单中加入这些item
        pop.add(myCut);
        pop.add(myCopy);
        pop.add(myPaste);
        pop.add(myDelete);
        pop.add(selectAll);
    }


    //以下为一些功能函数

    /**
     * 新建文件
     */
    public void newFile() {
        //如若文本已经修改则新建之前进行保存文件
        if (edited) {
            new DialogNew(frmTextPad, "Save");
        }
        //否则初始化文本界面
        else {
            //初始化jte界面
            lines = 1;
            np.hsp = 1;
            jta.setText("");
            //调整部件的可用状态
            menuItemSave.setEnabled(false);
            labelSave.setEnabled(false);
            labelUndo.setEnabled(false);
            labelRedo.setEnabled(false);
            menuItemUndo.setEnabled(false);
            menuItemRedo.setEnabled(false);
            np.setLocation(1, 1);
            filepath = null;

            panel.repaint();
        }
    }

    /**
     * 打开文件，进行读取文件的操作
     */
    public void open() {
        //如若文件进行了修改则通过调用保存操作
        if (!edited) {
            FileDialog dia1 = new FileDialog(frmTextPad, "打开");
            dia1.setVisible(true);
            String path = dia1.getDirectory() + dia1.getFile();

            if (!"".equals(path)) {
                filepath = path;
                //频繁添加时候可以用 StringBuilder
                StringBuilder context = new StringBuilder();
                //外部try-catch来检验文件是否打开成功
                try {
                    FileReader fr = new FileReader(path);
                    BufferedReader br = new BufferedReader(fr);
                    String message;
                    //内嵌套try-catch来检验读取文本内容时候是否出错
                    try {
                        while ((message = br.readLine()) != null) {
                            //可以写进去错误调试！
                            context.append(message).append("\n");
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //调整文本域参数以及部件的可用状态
                jta.setText(context.toString());
                lines = jta.getLineCount();
                menuItemSave.setEnabled(false);
                labelSave.setEnabled(false);

                panel.repaint();
            }
        }
        //如若文件进行了修改则通过调用保存操作
        else {
            new DialogOpen(frmTextPad, "Save");
        }
    }

    /**
     * 保存文件
     */
    public void save() {
        //如果文件不是通过打开来进行，则path为空，需要新建文件
        if (filepath == null) {
            saveToAnotherPath();
        }
        else {
            String context = jta.getText();
            //try-catch检验写入文件过程是否有IO流异常
            try {
                FileWriter fw = new FileWriter(filepath);
                fw.write(context);
                fw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            //设置保存之后的部件的可用状态
            menuItemSave.setEnabled(false);
            labelSave.setEnabled(false);
            edited = false;
        }
    }

    /**
     * 保存至其他路径
     */
    public void saveToAnotherPath() {

        FileDialog dia2 = new FileDialog(frmTextPad, "保存", FileDialog.SAVE);
        dia2.setVisible(true);
        //path后面加入。txt等形式就可以生成对应格式的文件
        String path = dia2.getDirectory() + dia2.getFile();

        if (!"".equals(path)) {
            filepath = path;
            File file = new File(path);
            if (!file.exists()) {
                //try-catch检验写入文件过程是否有IO流异常
                try {
                    if(file.createNewFile()) {
                        FileWriter fw = new FileWriter(path);
                        fw.write(jta.getText());
                        fw.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //调整部件的可用状态
            menuItemSave.setEnabled(false);
            labelSave.setEnabled(false);
            edited = false;
        }
    }

    /**
     * 剪切函数
     */
    public void cut() {
        jta.cut();
        //调整部件的可用状态
        edited = true;
        labelSave.setEnabled(true);
        menuItemSave.setEnabled(true);
    }

    /**
     *复制函数
     */
    public void copy() {
        jta.copy();
    }

    /**
     * 粘贴函数
     */
    public void paste() {

        jta.paste();
        //调整部件的可用状态
        edited = true;
        labelSave.setEnabled(true);
        menuItemSave.setEnabled(true);
        lines = jta.getLineCount();
        //todo 什么时候需要repaint?
        panel.repaint();
    }

    /**
     * 删除函数
     */
    public void delete() {

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        //我删除的实质是先保留当前粘贴板的状态
        Transferable t = cb.getContents(null);
        //剪切选择文本
        jta.cut();
        //还原粘贴板
        cb.setContents(t, null);
        //调整部件的可用状态
        edited = true;
        labelSave.setEnabled(true);
        menuItemSave.setEnabled(true);
        lines = jta.getLineCount();

        panel.repaint();
    }

    /**
     * 选中文本的全部内容
     */
    public void selectAll() {
        jta.selectAll();
    }

    /**
     * 在文本中直接添加时间和日期
     */
    public void addTime(){
        String dateTime = new SimpleDateFormat("HH:mm yyyy/MM/dd").format(new Date()) ;
        int pos = jta.getCaretPosition() ;
        jta.insert(dateTime, pos);
    }

    /**
     * 统计文本信息
     */
    private void statistics() {

        String text= jta.getText();
        String chineseRegex = "[\u4e00-\u9fff]";
        String numberRegex="[0-9]";
        String englishRegex="[a-zA-Z]";

        int chineseCount=0;
        int numberCount=0;
        int englishCount=0;

        for (int i = 0; i < text.length(); i++) {
            String des="" + text.charAt(i);
            if (des.matches(chineseRegex)) {
                chineseCount++;
            }
            if (des.matches(numberRegex)) {
                numberCount++;
            }
            if (des.matches(englishRegex)){
                englishCount++;
            }
        }

        String ch=Integer.toString(chineseCount);
        String nu=Integer.toString(numberCount);
        String eu=Integer.toString(englishCount);
        String lin=Integer.toString(MainFrame.lines);

        JOptionPane.showMessageDialog(frmTextPad,
                "文本域共有"+lin+"行\n中文字符有"+ch+"个\n"+"数字字符有"+nu+"个\n"+"英文字符有"+eu+"个\n"
                ,"统计信息", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 从文件添加文本
     */
    private void appendTextFromFile(){
        FileDialog dia1 = new FileDialog(frmTextPad, "打开");
        dia1.setVisible(true);
        String path = dia1.getDirectory() + dia1.getFile();

        if (!"".equals(path)) {
            filepath = path;
            //频繁添加时候可以用 StringBuilder
            StringBuilder context = new StringBuilder();
            //外部try-catch来检验文件是否打开成功
            try {
                FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);
                String message;
                //内嵌套try-catch来检验读取文本内容时候是否出错
                try {
                    while ((message = br.readLine()) != null) {
                        //可以写进去错误调试！
                        context.append(message).append("\n");
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //调整文本域参数以及部件的可用状态
            jta.append(context.toString());
            lines = jta.getLineCount();
            menuItemSave.setEnabled(true);
            labelSave.setEnabled(true);

            panel.repaint();
        }

    }

}
