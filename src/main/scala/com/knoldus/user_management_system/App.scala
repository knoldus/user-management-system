package com.knoldus.user_management_system

import com.knoldus.user_management_system.config.HttpConfig
import com.knoldus.user_management_system.routes.AccountServer
import zhttp.service.Server
import zio.ZIO

object App {
    def server = ZIO.scoped {
      for {
        config  <- ZIO.service[HttpConfig]
        httpApp <- AccountServer.httpRoutes
        start <- Server(httpApp).withBinding(config.host, config.port).make.orDie
        _ <- ZIO.logInfo(s"Server started on port: ${start.port}"
        )
        _ <- ZIO.never
      } yield ()
    }
}