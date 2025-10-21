# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keep class com.example.ardrawing.dbUtils.** {*;}
-keep class com.example.ardrawing.koinModule.** {*;}
-keep class org.opencv.android.OpenCVLoader { *; }

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#  Keep OpenCVLoader class completely
-keep class org.opencv.android.OpenCVLoader { *; }

# Ignore warnings for commonly used OpenCV classes
-dontwarn org.opencv.android.OpenCVLoader
-dontwarn org.opencv.android.Utils
-dontwarn org.opencv.core.Core
-dontwarn org.opencv.core.Mat
-dontwarn org.opencv.core.Size
-dontwarn org.opencv.imgproc.Imgproc

#  Keep core OpenCV classes if you use them directly (optional but recommended)
-keep class org.opencv.android.Utils { *; }
-keep class org.opencv.core.Core { *; }
-keep class org.opencv.core.Mat { *; }
-keep class org.opencv.core.Size { *; }
-keep class org.opencv.imgproc.Imgproc { *; }






