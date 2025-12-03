-dontwarn com.gemalto.jp2.**

# START PDFBox / JPXDecoder Keep Rules

# Keep the necessary decoder class used by PDFBox for JPX/JPEG 2000 files
-keep class com.gemalto.** { *; }

# Keep classes within the PDFBox filter package that reference the decoder
-keep class com.tom_roush.pdfbox.filter.** { *; }

# If using PDFBox's Android-specific integration, it's safer to keep it.
-keep class com.tom_roush.pdfbox.android.** { *; }

# END PDFBox / JPXDecoder Keep Rules