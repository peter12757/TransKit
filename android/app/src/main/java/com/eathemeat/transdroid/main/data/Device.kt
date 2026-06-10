package com.eathemeat.transdroid.main.data

data class Device(
    val id: Long,
    val name: String,
    val type: DeviceType,
    val macAddress: String,
    val status: ConnectionStatus = ConnectionStatus.Unconnected
)

enum class DeviceType { Mobile, Laptop, Audio }
enum class ConnectionStatus { Connected, Unconnected }
enum class TransferStatus { Pending, Transferring, Completed, Error }
enum class ConnectionType { Bluetooth, WiFi }





