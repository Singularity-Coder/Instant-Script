/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.singularitycoder.learnit.helpers

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// https://github.com/android/storage-samples/tree/main/ActionOpenDocumentTree

const val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"
const val MANAGE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1

internal fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

internal fun Activity.shouldShowRationaleFor(permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Activity.hasNotificationsPermission(): Boolean {
    return hasPermission(android.Manifest.permission.POST_NOTIFICATIONS)
}

fun Activity.hasAlarmPermission(): Boolean {
    return hasPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Activity.hasReadAudioFilesPermission(): Boolean {
    return hasPermission(android.Manifest.permission.READ_MEDIA_AUDIO)
}

fun Activity.showAppSettings() {
    val intent = Intent(
        /* action = */ Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        /* uri = */ Uri.fromParts("package", this.packageName, null)
    )
    this.startActivity(intent)
}

fun Activity.showNotificationSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    }
    this.startActivity(intent)
}

fun Context.askAlarmPermission() {
    val intent = Intent().apply {
        action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
    }
    startActivity(intent)
}

fun Activity.hasFullStoragePermissionApi30(): Boolean {
    val appOps = this.getSystemService(AppOpsManager::class.java)
    val mode = appOps.unsafeCheckOpNoThrow(
        MANAGE_EXTERNAL_STORAGE_PERMISSION,
        this.applicationInfo.uid,
        this.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

fun Activity.askFullStoragePermissionApi30() {
    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
    this.startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_PERMISSION_REQUEST)
}

fun Activity.showNotificationPermissionRationalePopup(callback: () -> Unit) {
    showAlertDialog(
        title = "Grant permission",
        message = "You must grant notification permission to use this App.",
        positiveBtnText = "Grant",
        negativeBtnText = "Cancel",
        positiveAction = {
            callback.invoke()
        }
    )
}

fun Activity.showNotificationSettingsPopup() {
    showAlertDialog(
        title = "Grant permission",
        message = "You must grant notification permission to use this App.",
        positiveBtnText = "Settings",
        negativeBtnText = "Cancel",
        positiveAction = {
            showNotificationSettings()
        }
    )
}

fun Activity.showSettingsPermissionRationalePopup(callback: () -> Unit) {
    showAlertDialog(
        title = "Grant permission",
        message = "You must grant this permission to use this App.",
        positiveBtnText = "Grant",
        negativeBtnText = "Cancel",
        positiveAction = {
            callback.invoke()
        }
    )
}

fun Activity.showSettingsPopup() {
    showAlertDialog(
        title = "Grant permission",
        message = "You must grant this permission to use this App.",
        positiveBtnText = "Settings",
        negativeBtnText = "Cancel",
        positiveAction = {
            showAppSettings()
        }
    )
}