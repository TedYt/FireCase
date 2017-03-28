package com.hytera.fcls.comutil;


/**
 * 项目发布设置，项目软件发放时参照此文件说明设置项目
 *
 *
 * 注意在调试华为手机默认logcat是关闭的，
 *     (1)进入拨号界面输入：*#*#2846579#*#*
 *     (2)依次选择ProjectMenu---后台设置----LOG设置---LOG开关 点击打开
 *      
 *  应用名、包名、高德 等的配置在 AndroidManifest.xml中设置
 */
public class ReleaseSetting {
	/**
	 *
	 * true时会有log信息输出，false没有log信息输出，
	 * 正式发布时改为false
	 */	
	public final static boolean IS_DEBUG_OPEN=true;


	/**是否将日志写入sd卡**/
	public final static boolean IS_WTIE_LOG_TO_FILE=false;
	


}
