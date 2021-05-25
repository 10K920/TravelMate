package com.example.fpauthenticationstep.MessagingActivities

// a class to store data related to sender and receiver when a user send a message
class InputMessage(
    val id: String,
    val text: String,
    val fromUID: String,
    val toUID: String,
    val timestamp: Long
)
{
constructor() : this(
    "",
    "",
    "",
    "",
    -1)
}