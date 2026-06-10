package com.eathemeat.transdroid.main.data

import android.net.Uri
import com.example.flashshare.ui.FileItem
import com.example.flashshare.ui.TransferStatus

data class FileItem(
    val id: Long = System.currentTimeMillis(),
    val uri: Uri? = null,
    val previewUrl: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String
) {
    val isImage: Boolean get() = mimeType.startsWith("image/")
}

data class TransferTask(
    val file: FileItem,
    var progress: Float = 0f,
    var status: com.example.flashshare.ui.TransferStatus = TransferStatus.Pending,
    var speed: Long = 0L // bytes per second
)