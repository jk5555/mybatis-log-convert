package com.kun.ui;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;

/**
 * @author kun.jiang@hand-china.com 2021-07-12 21:08
 */
public class NotificationUtils {
    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("ConvertLog.NotificationGroup", NotificationDisplayType.BALLOON, true);

    public static void showMessage(Project project, String message) {
        Notification success = NOTIFICATION_GROUP.createNotification(message, NotificationType.INFORMATION);
        Notifications.Bus.notify(success, project);
    }
}
