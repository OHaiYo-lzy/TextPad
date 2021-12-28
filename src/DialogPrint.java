import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;

/**
 * 打印功能对话框
 * @author Tyrion Lannister
 */
public class DialogPrint {
    /**
     * 打印函数
     */
    public static class PrintClass implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {

            //print string
            String str = MainFrame.jta.getText();
            //转换成Graphics2D
            Graphics2D g2 = (Graphics2D) graphics;
            //设置打印颜色为黑色
            g2.setColor(Color.black);
            //打印起点坐标
            double x = pageFormat.getImageableX();
            double y = pageFormat.getImageableY();

            if (pageIndex == 0) {
                //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
                //Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
                Font font = new Font("新宋体", Font.PLAIN, 9);
                //设置字体
                g2.setFont(font);

                float[] dash1 = {2.0f};
                //设置打印线的属性。
                //参数1为线宽 4为空白的宽度，5为虚线的宽度，6为偏移量
                g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));
                //字体高度
                float height = font.getSize2D();
                //以换行符号为标志打印内容
                String[] sp=str.split(String.valueOf('\n'));
                int line=0;
                for(String s : sp) {
                    g2.drawString(s, (float) x, (float) y + line * height);
                    line++;
                }
                //一条隐藏的版权信息
                String copyright="本打印服务由刘哲源提供 ^_^ ";
                g2.drawLine((int) x, (int) (y + line * height  + 10), (int) x + 446, (int) (y + line * height  + 10));
                g2.drawString(copyright, (float) x+320, (float) y + line * height+22);
                g2.drawLine((int) x, (int) (y + line * height  + 30), (int) x + 446, (int) (y + line * height  + 30));

                return PAGE_EXISTS;
            }
            return NO_SUCH_PAGE;
        }
    }

    public DialogPrint() {
        //new一个book
        Book book = new Book();
        //设置成竖打
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
        //通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        //纸张大小
        p.setSize(590, 840);
        //A4(595 X 842)设置打印区域，A4纸的默认X,Y边距是72
        p.setImageableArea(72, 72, 595, 842);

        pf.setPaper(p);

        //部署部件
        book.append(new PrintClass(), pf);

        //获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();

        //设置打印类
        job.setPageable(book);

        try {
            //可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
            boolean a = job.printDialog();
            if (a) {
                job.print();
            } else {
                job.cancel();
            }
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
