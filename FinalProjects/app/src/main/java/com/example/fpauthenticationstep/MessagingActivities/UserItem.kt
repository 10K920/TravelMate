package com.example.fpauthenticationstep.MessagingActivities

import android.content.Context

// class to store user information during messaging actions
class UserItem(
    val context: Context,
    val username: String,
    val picLoc: String,
    val email: String,
    val UID: String,
    val place: String)