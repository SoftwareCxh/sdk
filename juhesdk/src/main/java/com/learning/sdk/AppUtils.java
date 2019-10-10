package com.learning.sdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;

public class AppUtils {
    public static Map<String, String> getMap(Context ctx) {
        Map<String, String> map = new HashMap<>();
        map.put("os", "1");
        map.put("os_version", Build.VERSION.RELEASE);
        map.put("model", getBuildBrandModel());
        map.put("imei", getIMEI(ctx) == null ? "" : getIMEI(ctx));
        map.put("manufacturer",getBuildMANUFACTURER());
        map.put("android_id", getAndroidId(ctx));
        map.put("mac", getMac(ctx));
        map.put("device_type", isPhone(ctx) ? "1" : "2");
        map.put("screen_height", String.valueOf(getScreenHeights(ctx)));
        map.put("screen_width", String.valueOf(getScreenWidths(ctx)));
        String networkOperatorName = getNetworkOperatorName(ctx);
        switch (networkOperatorName) {
            case "":
                map.put("carrier", "4");
                break;
            case "中国移动":
                map.put("carrier", "1");
                break;
            case "中国联通":
                map.put("carrier", "2");
                break;
            case "中国电信":
                map.put("carrier", "3");
                break;
            default:
                map.put("carrier", "4");
                break;
        }
        map.put("connect_type", String.valueOf(getNetworkType(ctx)));
        map.put("orientation",String.valueOf(getOrientation(ctx)));

        return map;
    }

    /**
     * 获取屏幕方向，对应手助协议so字段
     *
     * @param context 应用上下文
     * @return 屏幕方向, 如：1竖屏 2横屏
     */
    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    public static String getAppVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static int getScreenWidths(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenHeights(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getBuildBrandModel() {
        return Build.MODEL;// Galaxy nexus 品牌类型
    }

    public static String getIMSI(Context context) {
        return getSubscriberId(context);
    }

    @SuppressLint("MissingPermission")
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!hasPermission(Manifest.permission.READ_PHONE_STATE, context)) {
            return null;
        }
        return tm.getSubscriberId();
    }


    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static String getBuildMANUFACTURER() {
        return Build.MANUFACTURER;// samsung 品牌
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceIdIMEI(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!hasPermission(Manifest.permission.READ_PHONE_STATE, context)) {
            return null;
        }
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }


    public static String getIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getDeviceIdIMEI(context);
        } else {
            deviceId = getAndroidId(context);
        }
        return deviceId;
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getMCC(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        return mcc;
    }

    public static int getMNC(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = mTelephonyManager.getNetworkOperator();
        int mnc = Integer.parseInt(operator.substring(3));
        return mnc;
    }

    public static int getLAC(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int mnc = getMNC(context);

        if (mnc == 0 || mnc == 1) {
            if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, context) &&
                    !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {
                return -1;
            } else {
                @SuppressLint("MissingPermission") GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
                if (location != null) {
                    return location.getLac();
                } else {
                    return -1;
                }

            }
        } else if (mnc == 2) {
            @SuppressLint("MissingPermission") CdmaCellLocation location1 = (CdmaCellLocation) tm.getCellLocation();
            return location1.getNetworkId();
        }
        return -1;
    }

    public static int getCID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int mnc = getMNC(context);
        if (mnc == 0 || mnc == 1) {
            if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, context) && !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {
                return -1;
            } else {
                @SuppressLint("MissingPermission") GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
                if (location == null) {
                    return -1;
                } else {
                    return location.getCid();
                }
            }
        } else if (mnc == 2) {
            @SuppressLint("MissingPermission") CdmaCellLocation location1 = (CdmaCellLocation) tm.getCellLocation();
            if (location1 == null) {
                return -1;
            } else {
                return location1.getBaseStationId();
            }
        }
        return -1;
    }

    /**
     * 未知
     */
    public static final int NETWORK_TYPE_UNKNOWN = 6;
    /**
     * WIFI
     */
    public static final int NETWORK_TYPE_WIFI = 1;
    /**
     * 未知移动网络
     */
    public static final int NETWORK_TYPE_UNKNOWN_MOBILE = 5;
    /**
     * 2G
     */
    public static final int NETWORK_TYPE_2G = 2;
    /**
     * 3G
     */
    public static final int NETWORK_TYPE_3G = 3;
    /**
     * 4G
     */
    public static final int NETWORK_TYPE_4G = 4;

    /**
     * 获取当前网络类型，对应手助协议net字段
     *
     * @param context 应用上下文
     * @return 网络类型，如：1 WI-FI
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        // 判断网络连通性
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // wifi
                return NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return NETWORK_TYPE_UNKNOWN_MOBILE;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NETWORK_TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NETWORK_TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NETWORK_TYPE_4G;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("GSM")) {
                            return NETWORK_TYPE_2G;
                        } else if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            return NETWORK_TYPE_3G;
                        } else {
                            return NETWORK_TYPE_UNKNOWN_MOBILE;
                        }
                }
            } else {
                return NETWORK_TYPE_UNKNOWN;
            }
        } else {
            return NETWORK_TYPE_UNKNOWN;
        }
    }

    /**
     * 获取手机UA，对应手助协议ua字段
     *
     * @return user-agent,如：Dalvik/2.1.0 (Linux; U; Android 5.0; vivo X5Pro D Build/LRX21M)
     */
    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
    }


    public static String getMac(Context context) {
        try {
            String strMac;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                strMac = getLocalMacAddressFromWifiInfo(context);
                return strMac;
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                strMac = getMacAddress(context);
                return strMac;
            } else {
                if (!TextUtils.isEmpty(getMacAddress())) {
                    strMac = getMacAddress();
                    return strMac;
                } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                    strMac = getMachineHardwareAddress();
                    return strMac;
                } else {
                    strMac = getLocalMacAddressFromBusybox();
                    return strMac;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
        }
        if ("".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());
            }

        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress0:" + e.toString());
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            Log.e("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
                    + "access wifi state is enabled");
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
     *
     */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean hasPermission(String permission, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean IMEICheck(String imei) {
        int sum1 = 0, sum2 = 0, temp = 0, total = 0, lastNum = 0;
        for (int i = 0; i < 14; i++) {
            if ((i % 2) == 0) {//奇数位
                sum1 = sum1 + Integer.parseInt(String.valueOf(imei.charAt(i)));
            } else {//偶数位
                temp = Integer.parseInt(String.valueOf(imei.charAt(i))) * 2;
                if (temp < 10) {
                    sum2 = sum2 + temp;
                } else {
                    sum2 = sum2 + 1 + temp - 10;
                }
            }
        }
        total = sum1 + sum2;
        //获取个位数
        if ((total % 10) == 0) {
            lastNum = 0;
        } else {
            lastNum = total % 10;
        }

        //校验
        if ((10 - lastNum) != Integer.parseInt(String.valueOf(imei.charAt(14))))
            return false;
        else
            return true;
    }


    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
