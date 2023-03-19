package com.knoldus.user_management_system.routes

import com.knoldus.user_management_system.exception.AccountError
import com.knoldus.user_management_system.model.{UserAccountDetails, UserDetails}
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

class UserAccountServerLive(accountService: AccountService) extends AccountServer {

  private val details = List(UserDetails(1, "Rahul", "Khowal", "rahul9239", "rahul.khowal@knoldus.com", "India", 9999591),
    UserDetails(2, "Harshita", "Mehta", "harshita111", "harshita.mehta@gmail.com", "India", 85485485),
    UserDetails(3, "Annu", "Gautam", "annuga987", "annu.gautam@gmail.com", "India", 658455544))

  private val baseEndpoint = endpoint.in("account")

  private val getAccountErrorOut = oneOf[AccountError](
    oneOfVariant(StatusCode.NotFound, jsonBody[AccountError.NotFound].description("Account not found."))
  )

  private val getAccountInputErrorOut = oneOf[AccountError](
    oneOfVariant(StatusCode.BadRequest, jsonBody[AccountError.InvalidInput].description("Invalid input."))
  )

  private val accountList = jsonBody[UserAccountDetails].example(UserAccountDetails(details))

  private val accountDetails = jsonBody[UserDetails].example(details(0))

  private val getAllAccountEndpoint =
    baseEndpoint.get
      .out(accountList)
      .errorOut(getAccountErrorOut)

  private val getAccountEndpoint =
      baseEndpoint.get
        .in(path[Int]("acc_id"))
        .out(accountDetails)
        .errorOut(getAccountErrorOut)

  private val putAccountEndpoint =
    baseEndpoint.put
      .in(path[Int]("acc_id"))
      .in(accountDetails)
      .out(accountList)
      .errorOut(getAccountInputErrorOut)

  private val postAccountEndpoint =
    baseEndpoint.post
      .in(accountDetails)
      .out(accountList)
      .errorOut(getAccountInputErrorOut)

  private val deleteAccountEndpoint =
    baseEndpoint.delete
      .in(path[Int]("acc_id"))
      .errorOut(getAccountInputErrorOut)


  private val allRoutes: Http[Any, Throwable, Request, Response] =
    ZioHttpInterpreter().toHttp(List(getAllAccountEndpoint.zServerLogic(_ => accountService.getAllAccountDetails),
      getAccountEndpoint.zServerLogic(accountId => accountService.getUserAccountDetail(accountId)),
      postAccountEndpoint.zServerLogic(accountDetails => accountService.addUserAccount(accountDetails)),
      deleteAccountEndpoint.zServerLogic(accountId => accountService.deleteUserAccountDetail(accountId)),
      putAccountEndpoint.zServerLogic(param => accountService.updateAccountDetails(param._1, param._2))))

  private val endpoints = {
    val endpoints = List(
      getAllAccountEndpoint,
      getAccountEndpoint,
      postAccountEndpoint,
      deleteAccountEndpoint,
      putAccountEndpoint
    )
    endpoints.map(_.tags(List("Account Endpoints")))
  }

  override def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]] =
     for {
      openApi       <- ZIO.succeed(OpenAPIDocsInterpreter().toOpenAPI(endpoints, "User Management Service", "0.1"))
      routesHttp    <- ZIO.succeed(allRoutes)
      endPointsHttp <- ZIO.succeed(ZioHttpInterpreter().toHttp(SwaggerUI[Task](openApi.toYaml)))
    } yield routesHttp ++ endPointsHttp
}