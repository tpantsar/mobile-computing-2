package com.codemave.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [Index("name", unique = true)]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message") val reminderTitle: String,
    @ColumnInfo(name = "location_x") val reminderLocation_x: Double?,
    @ColumnInfo(name = "location_y") val reminderLocation_y: Double?,
    @ColumnInfo(name = "reminder_time") val reminderTime: String,
    @ColumnInfo(name = "creation_time") val creationTime: LocalDateTime,
    @ColumnInfo(name = "creator_id") val creatorId: Long = 0,
    @ColumnInfo(name = "reminder_seen") val reminderSeen: Boolean = false
)