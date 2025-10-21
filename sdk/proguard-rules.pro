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