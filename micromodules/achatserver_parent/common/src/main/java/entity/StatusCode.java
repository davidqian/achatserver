package entity;

public class StatusCode {
    //返回正常
    public static final int OK = 2000;

    //返回失败
    public static final int ERROR = 2001;

    //登录失败
    public static final int LOGINERROR = 2002;

    //注册失败
    public static final int REGISTERERROR = 2003;

    //远程调用失败
    public static final int REMOTEERROR = 2004;

    //重复操作
    public static final int REPEATERROR = 2005;

}
