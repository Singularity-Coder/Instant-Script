package com.singularitycoder.learnit.helpers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object LearnItUtils {
    val gson: Gson = GsonBuilder().setLenient().create()
}

fun File.getBookId(): String = (this.nameWithoutExtension + "_" + this.extension).sanitize()