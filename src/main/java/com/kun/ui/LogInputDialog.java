package com.kun.ui;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.kun.convert.ClipboardUtils;
import com.kun.convert.ConvertUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author kun.jiang@hand-china.com 2021-07-05 20:53
 */
public class LogInputDialog extends DialogWrapper {

    private final Project project;

    NotificationGroup notificationGroup = new NotificationGroup("ConvertLog.NotificationGroup", NotificationDisplayType.BALLOON, true);

    private CenterTable centerTable;


    private static class CenterTable {
        private JLabel sql;
        private JTextArea sqlContent;

        private JLabel params;
        private JTextArea paramsContent;

        public CenterTable() {
            this.sql = new JLabel("sql：");
            this.sqlContent = new JTextArea();
            this.sqlContent.setToolTipText("这里输入完整的mybatis sql日志");
            this.sqlContent.setLineWrap(true);


            this.params = new JLabel("参数：");
            this.paramsContent = new JTextArea();
            this.paramsContent.setToolTipText("这里输入完整的mybatis 参数日志");
            this.paramsContent.setLineWrap(true);

        }


        public JLabel getSql() {
            return sql;
        }

        public void setSql(JLabel sql) {
            this.sql = sql;
        }

        public JTextArea getSqlContent() {
            return sqlContent;
        }

        public void setSqlContent(JTextArea sqlContent) {
            this.sqlContent = sqlContent;
        }

        public JLabel getParams() {
            return params;
        }

        public void setParams(JLabel params) {
            this.params = params;
        }

        public JTextArea getParamsContent() {
            return paramsContent;
        }

        public void setParamsContent(JTextArea paramsContent) {
            this.paramsContent = paramsContent;
        }
    }

    /**
     * Creates modal {@code DialogWrapper} that can be parent for other windows.
     * The currently active window will be the dialog's parent.
     *
     * @param project parent window for the dialog will be calculated based on focused window for the
     *                specified {@code project}. This parameter can be {@code null}. In this case parent window
     *                will be suggested based on current focused window.
     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
     * @see DialogWrapper#DialogWrapper(Project, boolean)
     */
    protected LogInputDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        setSize(600, 400);
        setResizable(true);
        setTitle("Mybatis Log Convert");
        initTable();
        init();
    }

    private void initTable() {
        centerTable = new CenterTable();
//        暂时先不这样做，感觉还不够成熟。
//        String content = ClipboardUtils.getContent();
//        if (ConvertUtils.isSql(content)) {
//            centerTable.getSqlContent().setText(content);
//        } else if (ConvertUtils.isParam(content)) {
//            centerTable.getParamsContent().setText(content);
//        }

    }

    /**
     * Factory method. It creates the panel located at the
     * north of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no input panel.
     *
     * @return north panel
     */
    @Override
    protected @Nullable JComponent createNorthPanel() {
        String text = "请复制完整的mybatis sql 日志粘贴到对应的输入框";
        JLabel title = new JLabel(text);
        //字体样式
        title.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        //水平居中
        title.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        title.setVerticalAlignment(SwingConstants.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.add(title);
        return jPanel;
    }

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no options panel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel jPanel = new JPanel();
        JBScrollPane jbScrollPane1 = new JBScrollPane(centerTable.getSqlContent());
        jbScrollPane1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //鼠标一进来就读取剪切板
                String content = ClipboardUtils.getContent();
                if (ConvertUtils.isSql(content)) {
                    String text = centerTable.getSqlContent().getText();
                    if (text == null || text.length() == 0) {
                        centerTable.getSqlContent().setText(content);
                    }
                } else if (ConvertUtils.isParam(content)) {
                    String text = centerTable.getParamsContent().getText();
                    if (text == null || text.length() == 0) {
                        centerTable.getParamsContent().setText(content);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        JBScrollPane jbScrollPane2 = new JBScrollPane(centerTable.getParamsContent());
        jbScrollPane2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //鼠标一进来就读取剪切板
                String content = ClipboardUtils.getContent();
                if (ConvertUtils.isParam(content)) {
                    String text = centerTable.getParamsContent().getText();
                    if (text == null || text.length() == 0) {
                        centerTable.getParamsContent().setText(content);
                    }
                } else if (ConvertUtils.isSql(content)) {
                    String text = centerTable.getSqlContent().getText();
                    if (text == null || text.length() == 0) {
                        centerTable.getSqlContent().setText(content);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //定义表格
        GridLayout gridLayout = new GridLayout(2, 1, 2, 2);
        jPanel.setLayout(gridLayout);

        jPanel.add(jbScrollPane1);
        jPanel.add(jbScrollPane2);

        return jPanel;
    }


    /**
     * Creates panel located at the south of the content pane. By default that
     * panel contains dialog's buttons. This default implementation uses {@code createActions()}
     * and {@code createJButtonForAction(Action)} methods to construct the panel.
     *
     * @return south panel
     */
    @Override
    protected JComponent createSouthPanel() {

        JButton convert = new JButton("转换");
        //水平居中
        convert.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        convert.setVerticalAlignment(SwingConstants.CENTER);
        //添加事件
        convert.addActionListener(e -> {
            String sqlContent = centerTable.getSqlContent().getText();
            String paramsContent = centerTable.getParamsContent().getText();
            String result = ConvertUtils.convert(sqlContent, paramsContent);
            showResult(result);
        });

        JPanel jPanel = new JPanel();
        jPanel.add(convert);
        jPanel.add(convert);
        return jPanel;
    }

    private void showResult(String text) {
        if (text == null || text.length() <= 0) {
            Messages.showErrorDialog(project, "请按规则填写对应sql或参数", "解析错误");
            return;
        } else if (text.contains("?")) {
            Messages.showErrorDialog(project, "参数与sql不匹配，请检查", "解析错误");
            return;
        }

        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        this.close(0);

        String message = "转换成功！已将结果复制到剪切板，直接粘贴即可";
        Notification success = notificationGroup.createNotification(message, NotificationType.INFORMATION);
        Notifications.Bus.notify(success, project);

    }


}
