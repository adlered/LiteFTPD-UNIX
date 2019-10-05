package pers.adlered.liteftpd.tool;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>AutoInputStream can analyze Chinese encoding from InputStream, and print it out correctly.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AutoInputStream {
    private InputStream inputStream = null;
    private int cacheSize = 0;
    private PrivateVariable privateVariable = null;

    public AutoInputStream(InputStream inputStream, int cacheSize, PrivateVariable privateVariable) {
        this.inputStream = inputStream;
        this.cacheSize = cacheSize;
        this.privateVariable = privateVariable;
    }

    public String readLineAuto() throws IOException {
        byte[] storage = new byte[cacheSize];
        int c2Cursor = 0;
        while (true) {
            int available = 0;
            try {
                while (available == 0) {
                    available = inputStream.available();
                    Thread.sleep(5);
                }
            } catch (IOException IOE) {
                Logger.log(Types.SEND, Levels.WARN, "Auto Input Stream stopped.");
                break;
            } catch (InterruptedException IE) {
            }
            byte[] cache = new byte[available];
            inputStream.read(cache);
            int cursor = 0;
            int relativeLength = c2Cursor + cache.length;
            if (relativeLength <= cacheSize) {
                for (int i = c2Cursor; i < relativeLength; i++) {
                    storage[i] = cache[cursor];
                    ++cursor;
                    ++c2Cursor;
                }
                // Check if space exists, break it.
                if (new String(storage, StandardCharsets.UTF_8).indexOf("\n") != -1) {
                    break;
                }
            } else {
                break;
            }
        }
        // Clean wasted space
        int storageCursor = 0;
        int firstEmptyMark = -1;
        boolean marked = false;
        for (byte i : storage) {
            if (i == 0) {
                if (!marked) {
                    marked = true;
                    firstEmptyMark = storageCursor;
                }
            }
            ++storageCursor;
        }
        if (firstEmptyMark == -1) firstEmptyMark = storage.length;
        // Init a fit size bytes.
        byte[] cleaned = new byte[firstEmptyMark];
        for (int i = 0; i < firstEmptyMark; i++) {
            cleaned[i] = storage[i];
        }
        String UTF8 = new String(cleaned, StandardCharsets.UTF_8);
        String GB2312 = new String(cleaned, "GB2312");
        String charset = CharsetSelector.getCharset(cleaned);
        if (!privateVariable.isEncodeLock()) {
            privateVariable.setEncode(charset);
            if (charset.equals("GB2312")) {
                privateVariable.setEncodeLock(true);
            }
        }
        String bestMatch = new String(cleaned, charset);
        return bestMatch;
    }

    public String readLineInUTF8() throws IOException {
        return "";
    }

    public String readLineInGB2312() throws IOException {
        return "";
    }
}
