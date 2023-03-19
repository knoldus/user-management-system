package com.knoldus.user_management_system.model

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.util.UUID


case class UserDetails(accountId: Int,
                          firstName: String,
                          LastName:String,
                          userName: String,
                          email: String,
                          country: String,
                          contactNo: Long)


object UserDetails {
  implicit val jsonCodec: JsonCodec[UserDetails] = DeriveJsonCodec.gen
  implicit val schema: Schema[UserDetails] = Schema.derived
}