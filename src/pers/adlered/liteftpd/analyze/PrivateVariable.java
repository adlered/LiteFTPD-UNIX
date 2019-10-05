package pers.adlered.liteftpd.analyze;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Store values when service is running to make sure every model works together.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class PrivateVariable {
    public boolean interrupted = false;
    public String encode = "UTF-8";
    public String reason = null;
    //When translating, turn the timeout on to avoid timeout & disconnect.
    private boolean timeoutLock = false;
    //If Encode Lock is on, smart encode will not working.
    private boolean encodeLock = false;
    //Rest
    private long rest = 0l;

    public boolean isEncodeLock() {
        return encodeLock;
    }

    public void setEncodeLock(boolean encodeLock) {
        this.encodeLock = encodeLock;
    }

    public boolean isTimeoutLock() {
        return timeoutLock;
    }

    public void setTimeoutLock(boolean timeoutLock) {
        this.timeoutLock = timeoutLock;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public long getRest() {
        return rest;
    }

    public void setRest(long rest) {
        this.rest = rest;
    }

    public void resetRest() {
        rest = 0l;
    }
}
