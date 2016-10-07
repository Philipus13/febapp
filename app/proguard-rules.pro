# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\USER\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keep class !android.support.v7.internal.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep class android.support.v7.widget.LinearLayoutManager { *; }
-keep public class * extends android.support.v7.widget.RecyclerView$LayoutManager {
    public <init>(...);
}
-keep public class * extends android.support.v7.widget.DrawerLayout$LayoutManager {
    public <init>(...);
}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
