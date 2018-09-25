package fr.corenting.edcompanion.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import android.widget.Toast;

import fr.corenting.edcompanion.R;

public class MiscUtils {

    public static void putTextInClipboard(Context ctx, String label, String content,
                                          boolean displayNotification) {
        try {
            ClipboardManager clipboard = (ClipboardManager)
                    ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard == null) {
                throw new Exception(ctx.getString(R.string.content_copied_to_clipboard_error));
            }
            ClipData clip = ClipData.newPlainText(label, content);
            clipboard.setPrimaryClip(clip);

            if (displayNotification) {
                Toast t = Toast.makeText(ctx, R.string.content_copied_to_clipboard, Toast.LENGTH_SHORT);
                t.show();
            }
        } catch (Exception e) {

            if (displayNotification) {
                Toast t = Toast.makeText(ctx, R.string.content_copied_to_clipboard_error,
                        Toast.LENGTH_SHORT);
                t.show();
            }

        }
    }

    public static void startIntentWithFadeAnimation(Context ctx, Intent i) {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(ctx,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

        ctx.startActivity(i, bundle);
    }


}
