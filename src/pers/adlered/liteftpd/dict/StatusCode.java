package pers.adlered.liteftpd.dict;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Status code.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class StatusCode {

    /**
     * Preliminary affirmative reply
     */

    // Service is ready, will be start in ? min.
    public static final int READY = 120;
    // Data connection opened, transmission is starting.
    public static final int OPEN = 125;
    // File status OK, ready to open data connect.
    public static final int STATUSOK = 150;

    /**
     * A definite and complete answer
     */

    // Command success.
    public static final int SUCCESS = 200;
    // Cannot execute command, too much command at the same time.
    public static final int MUCHCOMMAND = 202;
    // System status / System helping answer.
    public static final int STATUS = 211;
    // Directory status.
    public static final int DIRSTATUS = 212;
    // File status.
    public static final int FILESTATUS = 213;
    // Helping message.
    public static final int HELPING = 214;
    // NAME System type.
    public static final int NAME = 215;
    // Service is ready, can execute new user's request.
    public static final int SERVICEREADY = 220;
    // Service stopped control connection.
    public static final int SERVICESTOP = 221;
    // Data connection opened, no any transmission running.
    public static final int DATAOPEN = 225;
    // Data connection closed with successful.
    public static final int CLOSED = 226;
    // Enter passive mode.
    public static final int PASSIVE = 227;
    // User logged in.
    public static final int LOGGED = 230;
    // File request correct, done.
    public static final int CORRECT = 250;
    // Created "PATHNAME".
    public static final int CPATH = 257;

    /**
     * Intermediate affirmative response
     */

    // Password required.
    public static final int PASSREQ = 331;
    // Need login.
    public static final int LOGIN = 332;
    // Waiting for file execution response.
    public static final int WAIT = 350;

    /**
     * Complete Answer to Transient Negation
     */

    // Cannot open data connection.
    public static final int CANTOPEN = 425;
    // Connection closed; Transfer aborted.
    public static final int ABORTED = 426;
    //File unavailable。
    public static final int UNAVAILABLE = 450;
    //Processing local errors.
    public static final int ERRORS = 451;
    //Not enough storage space.
    public static final int OUTOFSPACE = 452;

    /**
     * Permanent negative completion reply
     */

    //Command not understood.
    public static final int CMDUNKNOWN = 500;
    //A syntax error in the argument
    public static final int ERRSYNTAX = 501;
    //Command not executed.
    public static final int NOTRUN = 502;
    //Error command sequence.
    public static final int ERRSEQUENCE = 503;
    //Command arguments not executed.
    public static final int ERRARGS = 504;
    //Not logged in.
    public static final int NOTLOGIN = 530;
    //Need account while uploading files.
    public static final int NEEDACCOUNT = 532;
    //File not found / no permission.
    public static final int ERRTARGET = 550;
    //Unknown type of the page.
    public static final int UNTYPE = 551;
    //Out of storage space distribution.
    public static final int OUTSPACELIMIT = 552;
    //Filename not allowed.
    public static final int INVALIDFILENAME = 553;
}
