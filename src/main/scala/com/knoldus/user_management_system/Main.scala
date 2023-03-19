package com.knoldus.user_management_system

import com.knoldus.user_management_system.config.HttpConfig
import com.knoldus.user_management_system.routes.AccountServer
import com.knoldus.user_management_system.service.AccountService
import io.netty.channel.{ChannelFactory, ServerChannel}
import zhttp.service.{EventLoopGroup, Server}
import zhttp.service.server.ServerChannelFactory
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    App.server.provide(AccountServer.live,
      AccountService.live,
      HttpServerSettings.default,
      HttpConfig.live,
      ZLayer.Debug.tree)
  }
}