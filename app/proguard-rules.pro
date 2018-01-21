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
-dontshrink
-dontoptimize
-libraryjars libs

-assumenosideeffects class android.util.Log { *; }

-keep class com.crashlytics.** { *; }
-keepclassmembers class com.crashlytics.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

-keep class com.google.code.gson.** { *; }
-keep class com.google.maps.android.** { *; }
-keep class com.android.support.** { *; }

-keep interface com.android.support.** { *; }



-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}



##---------------End: proguard configuration for Gson  ----------

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#Crashalytics
-keep class io.fabric.sdk.** {*;}


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


