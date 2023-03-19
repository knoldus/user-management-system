package com.knoldus.user_management_system.model

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

case class UserAccountDetails(accountDetails: List[UserDetails])

object UserAccountDetails {
  implicit val jsonCodec: JsonCodec[UserAccountDetails] = DeriveJsonCodec.gen
  implicit val schema: Schema[UserAccountDetails] = Schema.derived
}