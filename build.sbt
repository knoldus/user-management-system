ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "user_management_system"
  )
val tapirVersion = "1.0.2"
val zioVersion = "2.0.1"
val zioConfigVersion = "3.0.7"
val ulidVersion = "22.12.6"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
  "io.d11" %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "org.wvlet.airframe" %% "airframe-ulid" % ulidVersion

)