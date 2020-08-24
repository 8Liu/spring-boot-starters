package cn.com.siss.spring.boot.core.autoconfigure;

public class CorsConstant {

    public final static String[] DEFAULT_ALLOWED_ORIGINS = {"*"};

    public final static String[] DEFAULT_ALLOWED_HEADERS = {"*"};

    public final static String[] DEFAULT_ALLOWED_METHODS = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"};

    public final static boolean DEFAULT_ALLOW_CREDENTIALS = true;

    public final static long DEFAULT_MAX_AGE = 1800;

    public final static String SIMPLE_ALLOWED_HEADERS = "*,Accept,Accept-Language,Accept-Encoding,Content-Language,Last-Event-ID,Content-Type";
}
