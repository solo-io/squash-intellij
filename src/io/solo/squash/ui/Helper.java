package io.solo.squash.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SelectFromListDialog;

import javax.swing.*;

import static com.intellij.openapi.ui.Messages.showInputDialog;
import static com.intellij.openapi.ui.Messages.showMessageDialog;
import static com.intellij.openapi.ui.Messages.showYesNoDialog;

public class Helper {

    public static Object showSelection(Project project, String title, Object[] options, SelectFromListDialog.ToStringAspect aspect) {
        try {
            SelectFromListDialog dlg = new SelectFromListDialog(project, options, aspect, title, ListSelectionModel.SINGLE_SELECTION);
            if (dlg.showAndGet()) {
                Object[] sel = dlg.getSelection();
                if (sel.length > 0) {
                    return sel[0];
                }
            }
        }
        catch (Exception ex) {
        }
        return null;
    }

    public static String getInput(Project project, String title, String message) {
        return showInputDialog(project, title, message, AllIcons.General.QuestionDialog);
    }

    public static void showInfoMessage(Project project, String title, String message) {
        showMessageDialog(project, message, title, AllIcons.General.Information);
    }

    public static void showErrorMessage(Project project, String title, String message) {
        showMessageDialog(project, message, title, AllIcons.General.Error);
    }

    public static boolean askConfirmation(Project project, String title, String message) {
        return showYesNoDialog(project,message,title, AllIcons.General.QuestionDialog) == 0;
    }
}
