package com.knoldus.user_management_system.routes

import com.knoldus.user_management_system.service.AccountService
import sttp.apispec.openapi.circe.yaml.*
import sttp.model.StatusCode
import sttp.tapir.PublicEndpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.*
import zhttp.http.{Http, HttpApp, Request, Response}
import zio.*

trait AccountServer {
  def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]]
}

object AccountServer {

  lazy val live: ZLayer[AccountService, Nothing, AccountServer] = ZLayer {
    for {
      accountService <- ZIO.service[AccountService]
    } yield UserAccountServerLive(accountService)
  }

  def httpRoutes: ZIO[AccountServer, Nothing, HttpApp[Any, Throwable]] =
    ZIO.serviceWithZIO[AccountServer](_.httpRoutes)
}