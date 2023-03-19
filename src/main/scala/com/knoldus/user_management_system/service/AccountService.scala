package com.knoldus.user_management_system.service

import com.knoldus.user_management_system.exception.AccountError
import com.knoldus.user_management_system.model.{UserDetails, UserAccountDetails}
import zio.{ZIO, ZLayer}

import scala.collection.mutable.ListBuffer

trait AccountService {
  def getAllAccountDetails: ZIO[Any, Nothing, UserAccountDetails]

  def getUserAccountDetail(accountId: Int): ZIO[Any, AccountError.NotFound, UserDetails]

  def updateAccountDetails(accountId: Int, accountDetails: UserDetails): ZIO[Any, AccountError.InvalidInput, UserAccountDetails]

  def addUserAccount(accountDetails: UserDetails): ZIO[Any, AccountError.InvalidInput, UserAccountDetails]

  def deleteUserAccountDetail(accountId: Int): ZIO[Any, AccountError.InvalidInput, Unit]
}

object AccountService {
  lazy val live: ZLayer[Any, Nothing, AccountService] = ZLayer {
    ZIO.succeed(AcountServiceImpl())
  }
}

class AcountServiceImpl extends  AccountService {

  private var account_Details = ListBuffer(UserDetails(1,"Rahul","Khowal","rahul9239","rahul.khowal@knoldus.com","India",9999591),
    UserDetails(2,"Raviyanshu","Singh","ravi111","raviyanshu.singh@knoldus.com","India",85485485),
    UserDetails(3,"Ayush","tiwari","ayush001","ayush.tiwari@knoldus.com","India",658455544))

  override def getAllAccountDetails: ZIO[Any, Nothing, UserAccountDetails] = {
    ZIO.logInfo("Get all Account Details") *>
      ZIO.succeed(UserAccountDetails(account_Details.toList))
  }

  override def getUserAccountDetail(accountId: Int): ZIO[Any, AccountError.NotFound, UserDetails] = {
    ZIO.logInfo(s" User Account Details : $accountId .") *>
      ZIO.fromOption(addAccountDetails(accountId))
      .mapError(_ => AccountError.NotFound(s"User Account Details not found for $accountId"))
      .debug(s"User Account Details not found for $accountId")
  }
  private val addAccountDetails = (accountId :Int) => account_Details.find(accDetails => accDetails.accountId.equals(accountId))

  override def updateAccountDetails(accountId: Int, updatedAccountDetails: UserDetails): ZIO[Any, AccountError.InvalidInput, UserAccountDetails] = {
    account_Details.find(accDetails => accDetails.accountId.equals(accountId)) match
      case Some(accountDetails) =>
        account_Details.update(account_Details.indexOf(accountDetails), updatedAccountDetails)
        ZIO.logInfo(s"Update User Account Details for accountId : $accountId") *>
            ZIO.succeed(UserAccountDetails(account_Details.toList))
      case _ =>
        ZIO.logInfo(s"Update User Account Details for accountId :  $accountId") *>
          ZIO.fail(AccountError.InvalidInput(s"Update is failed. Account Id is not available $accountId"))
  }
  
  override  def addUserAccount(accountDetails: UserDetails): ZIO[Any, AccountError.InvalidInput, UserAccountDetails] = {
    account_Details.find(accDetails => accDetails.accountId.equals(accountDetails.accountId)) match {
     case None =>
       account_Details += accountDetails
        ZIO.logInfo(s"Insert Account Details for accountId : ${accountDetails.accountId}") *>
          ZIO.succeed(UserAccountDetails(account_Details.toList))
     case Some(accDetails) =>
       ZIO.logInfo(s"new  Account Details is not added. Already exist same accountId : ${accDetails.accountId}") *>
         ZIO.fail(AccountError.InvalidInput(s"Insert is failed. Account Id is already available ${accDetails.accountId}"))
    }
  }
  
  override def deleteUserAccountDetail(accountId: Int): ZIO[Any, AccountError.InvalidInput, Unit] = {
    account_Details.find(accDetail => accDetail.accountId.equals(accountId)) match {
      case Some(accDetail) =>
        account_Details -= accDetail
        ZIO.logInfo(s"Deleted User Account Details for accountId : $accountId") *>
          ZIO.succeed(())
      case _ =>
        ZIO.logInfo(s"new Account Details couldn't be deleted. Account Id is not exist : $accountId") *>
          ZIO.fail(AccountError.InvalidInput(s"Delete is failed. Account Id is not found $accountId"))
    }
  }
}