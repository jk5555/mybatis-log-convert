package com.kun.ui;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;

/**
 * @author kun.jiang@hand-china.com 2021-07-12 21:08
 */
public class NotificationUtils {
    private static final String NOTIFICATION_GROUP = "ConvertLog.NotificationGroup";

    public static void showMessage(Project project, String message) {
        Notification notification = new Notification(NOTIFICATION_GROUP,"", message, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, project);
    }
}
