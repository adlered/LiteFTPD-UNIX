package pers.adlered.liteftpd.analyze;

public class PrivateVariable {
    public boolean interrupted = false;
    public String encode = "UTF-8";
    //When translating, turn the timeout on to avoid timeout & disconnect.
    private boolean timeoutLock = false;

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
}
