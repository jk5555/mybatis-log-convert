package com.kun.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.kun.convert.ClipboardUtils;
import com.kun.convert.ConvertUtils;

/**
 * @author kun
 */
public class MybatisLogTransAction extends AnAction {

    public static final String SQL_START_STR = "Preparing: ";
    public static final String PARAMS_START_STR = "Parameters: ";
    public static final String CHANGE_LINE = "\n";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        //获取编辑器对象
        Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
        //获取光标选中文本
        assert editor != null;
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        String selectedText = currentCaret.getSelectedText();

        if (!currentCaret.hasSelection() || selectedText == null || selectedText.length() == 0) {
            NotificationUtils.showMessage(project, "请先用光标选定mybatis的sql日志和参数日志");
            return;
        }

        int strNum = ConvertUtils.strNum(selectedText, ": ");
        if (strNum != 2) {
            String message;
            if (strNum < 2) {
               message = "光标选定mybatis的sql日志和参数日志时，尽量包含“Preparing: ”和“Parameters: ” ";
            } else {
                message = "选定内容格式不对，请检查选择的内容是否为一对对应的sql日志和参数日志";
            }
            NotificationUtils.showMessage(project, message);
            return;
        }

        try {
            //截取sql
            int sqlStart = selectedText.indexOf(SQL_START_STR);
            int paramsStart = selectedText.indexOf(PARAMS_START_STR);
            String subSql = selectedText.substring(sqlStart + SQL_START_STR.length(), paramsStart);
            String sql = subSql.substring(0, subSql.lastIndexOf(CHANGE_LINE));
            //截取参数
            String subParams = selectedText.substring(paramsStart + PARAMS_START_STR.length());
            String params = subParams.contains(CHANGE_LINE) ? subParams.substring(0, subParams.lastIndexOf(CHANGE_LINE)) : subParams;
            //转换
            String convert = ConvertUtils.convert(sql, params);
            if (convert == null || convert.length() == 0) {
                NotificationUtils.showMessage(project, "转换失败，请检查参数是否正确");
            }
            ClipboardUtils.setContent(convert);
            NotificationUtils.showMessage(project, "转换成功！已将结果复制到剪切板，直接粘贴即可");

        } catch (Exception exception) {
            exception.printStackTrace();
            NotificationUtils.showMessage(project, "转换失败！");
        }

    }
}
