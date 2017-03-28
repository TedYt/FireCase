package com.hytera.fcls.comutil;
import com.hytera.fcls.BuildConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log管理类
 */

public final class Log {
    private static String tag = "LogManager";
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 86400 * 20;// sd卡中日志文件的最多保存天数
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String MYLOGFILEName = ".txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式

    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static String clock = "";
    public static final boolean LOGDEBUG = BuildConfig.LOG_DEBUG; // 发布版位false

    public Log(String tag) {
        this.tag = tag;
    }

    public static void d(String msg) {
        if (LOGDEBUG) {

//			Log.d(tag, msg);
            log(tag, msg, 'd');
        }
    }

    public static void d(String TAG, String msg) {
        if (LOGDEBUG) {

//			Log.d(tag, msg);
            log(TAG, msg, 'd');
        }
    }

    public static void e(String msg) {
        if (LOGDEBUG) {
            log(tag, msg, 'e');
        }
    }

    public static void e(String TAG, String msg) {
        if (LOGDEBUG) {
            log(TAG, msg, 'e');
        }
    }

    public static void i(String msg) {
        if (LOGDEBUG) {
            log(tag, msg, 'i');
        }
    }

    public static void i(String TAG, String msg) {
        if (LOGDEBUG) {
            log(TAG, msg, 'i');
        }
    }

    public static void w(String msg) {
        if (LOGDEBUG) {
            log(tag, msg, 'w');
        }
    }

    public static void w(String TAG, String msg) {
        if (LOGDEBUG) {
            log(TAG, msg, 'w');
        }
    }

    public static void v(String msg) {
        if (LOGDEBUG) {
            log(tag, msg, 'v');
        }
    }

    public static void v(String TAG, String msg) {
        if (LOGDEBUG) {
            log(TAG, msg, 'v');
        }
    }

    /**
     * 输出日志信息
     *
     * @param tag   日志标签
     * @param msg   日志内容
     * @param level 日志等级
     */
    private static void log(String tag, String msg, char level) {
        synchronized (clock) {
//import com.hytera.fcls.comutil.Log;
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
                android.util.Log.e(tag, msg);
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.w(tag, msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.d(tag, msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.i(tag, msg);
            } else {
                android.util.Log.v(tag, msg);
            }
            if (ReleaseSetting.IS_WTIE_LOG_TO_FILE)
                writeLogtoFile(String.valueOf(level), tag, msg);
        }
    }

    /**
     * 打开日志文件并写入日志    该方法有问题暂时不用该方法，写日志操作需在子线程中
     *
     * @param mylogtype
     * @param tag
     * @param text
     */
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date(System.currentTimeMillis());
        String needWriteFiel = logfile.format(nowtime);//日志文件名
        String needWriteMessage = myLogSdf.format(nowtime) + "  " + mylogtype
                + "  " + tag + "  " + text;

        File file = new File(DirUtil.LOG_FILE_PATH, needWriteFiel
                + MYLOGFILEName);
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除前几天的日志
     */
    public static void LogFileInit() {

//		  File file = new File(DirUtil.LOG_FILE_PATH);//strPath为路径
//		  TimeFormat timeFormat=new TimeFormat();
//	      String fileName[]=  file.list();
//	      if(fileName==null){
//	    	  return;
//	      }
//	      String[] testString;
//	      for(int i=0;i<fileName.length;i++){
//	    	  String logTitle= fileName[i];
//	    	  testString =  logTitle.split(".tx");
//			  int nowTime=timeFormat.getSysTimestamp();//获取系统时间S
//			  int endTime= timeFormat.getTimestamp("yyyy-MM-dd",testString[0]);//结束时间
//	    	  if(nowTime> endTime && (nowTime-endTime)>SDCARD_LOG_FILE_SAVE_DAYS){
//	    		  delFile(logTitle);
//	    	  }
//	      }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile(String fileName) {// 删除日志文件
        File file = new File(DirUtil.LOG_FILE_PATH, fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
