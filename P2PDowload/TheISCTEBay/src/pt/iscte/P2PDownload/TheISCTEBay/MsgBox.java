package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JOptionPane;

public final class MsgBox
{
		
    public static void info(String message) {
        info(message, oNomeDoMetodoQueMeChamou());
    }
    public static void info(String message, String caller) {
        mostra(message, caller, JOptionPane.INFORMATION_MESSAGE);
    }

    static void erro(String message) {
        erro(message, oNomeDoMetodoQueMeChamou());
    }
    public static void erro(String message, String caller) {
        mostra(message, caller, JOptionPane.ERROR_MESSAGE);
    }

    public static void mostra(String message, String title, int iconId) {
        setClipboard(title+":"+NEW_LINE+message);
        JOptionPane.showMessageDialog(null, message, title, iconId);
    }
    private static final String NEW_LINE = System.lineSeparator();

    public static String oNomeDoMetodoQueMeChamou() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public static void setClipboard(String message) {
        CLIPBOARD.setContents(new StringSelection(message), null);
        // nb: we don't respond to the "your content was splattered"
        //     event, so it's OK to pass a null owner.
    }
    private static final Toolkit AWT_TOOLKIT = Toolkit.getDefaultToolkit();
    private static final Clipboard CLIPBOARD = AWT_TOOLKIT.getSystemClipboard();

}


