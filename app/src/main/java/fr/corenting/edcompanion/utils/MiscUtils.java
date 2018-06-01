package fr.corenting.edcompanion.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import fr.corenting.edcompanion.R;

public class MiscUtils {

    public static boolean putTextInClipboard(Context ctx, String label, String content) {
        try {
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, content);
            clipboard.setPrimaryClip(clip);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static void putTextInClipboardWithNotification(Context ctx, String label, String content) {
        if (putTextInClipboard(ctx, label, content)) {
            Toast t = Toast.makeText(ctx, R.string.content_copied_to_clipboard, Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            Toast t = Toast.makeText(ctx, R.string.content_copied_to_clipboard_error, Toast.LENGTH_SHORT);
            t.show();
        }
    }
}
