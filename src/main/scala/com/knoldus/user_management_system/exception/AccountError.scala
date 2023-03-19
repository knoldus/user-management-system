package com.knoldus.user_management_system.exception

import sttp.tapir.Schema
import zio.json.{DeriveJsonCodec, JsonCodec}

sealed trait AccountError

object AccountError {
  implicit lazy val codec: JsonCodec[AccountError] = DeriveJsonCodec.gen

  case class InvalidInput(error: String) extends AccountError

  object InvalidInput {
    implicit lazy val codec: JsonCodec[InvalidInput] = DeriveJsonCodec.gen
    implicit lazy val schema: Schema[InvalidInput] = Schema.derived
  }

  case class NotFound(message: String) extends AccountError

  object NotFound {
    implicit lazy val codec: JsonCodec[NotFound] = DeriveJsonCodec.gen
    implicit lazy val schema: Schema[NotFound] = Schema.derived
  }
}