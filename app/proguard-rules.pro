# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/anubhav/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Add this global rule
-keepattributes Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.imagesoftware.anubhav.vacmet.model.** {
  *;
}

-keepattributes *Annotation*

-keepattributes SourceFile,LineNumberTable

-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class com.firebase.** { *; }
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepclassmembers class com.imagesoftware.anubhav.vacmet.database.converters.** {
  *;
}
-keepclassmembers class com.imagesoftware.anubhav.vacmet.database.entities.** {
  *;
}
-keepclassmembers class com.imagesoftware.anubhav.vacmet.database.translators.** {
  *;
}

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
