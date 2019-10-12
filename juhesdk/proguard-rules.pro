## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.
##-keepattributes SourceFile,LineNumberTable
#
## If you keep the line number information, uncomment this to
## hide the original source file name.
##-renamesourcefileattribute SourceFile
#
#-optimizationpasses 5
#
##混淆时不会产生形形色色的类名
#-dontusemixedcaseclassnames
#
##指定不去忽略非公共的库类
#-dontskipnonpubliclibraryclasses
#
##不预校验
##-dontpreverify
#
##不优化输入的类文件
#-dontoptimize
#
#-ignorewarnings
#
#-verbose
#
##优化
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
##保护内部类
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#
#-keep class com.bytedance.sdk.openadsdk.** {*;}
#-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
#-keep class com.ss.sys.ces.* {*;}
#
##GDT
#-dontwarn com.androidquery.**
#-keep class com.androidquery.** { *;}
#
#-dontwarn tv.danmaku.**
#-keep class tv.danmaku.** { *;}
#
#-dontwarn androidx.**
#
#
##BD
#-keepclassmembers class * extends android.app.Activity {
# public void *(android.view.View);
#}
#-keepclassmembers enum * {
# public static **[] values();
# public static ** valueOf(java.lang.String);
#}
#-keep class com.baidu.mobads.*.** { *; }