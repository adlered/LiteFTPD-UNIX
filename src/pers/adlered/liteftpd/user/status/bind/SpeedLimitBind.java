package pers.adlered.liteftpd.user.status.bind;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>限速Bind</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-12 16:12
 **/
public class SpeedLimitBind {
    private int uploadSpeed;
    private int downloadSpeed;

    public SpeedLimitBind(int uploadSpeed, int downloadSpeed) {
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
    }

    public int getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(int uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(int downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }
}
