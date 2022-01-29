package com.xanpool.server

import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Hellohttp4sServer {

  def run: IO[Nothing] = {
    for {
      client <- EmberClientBuilder.default[IO].build
      helloWorldAlg = HelloWorld.impl
      jokeAlg = Jokes.impl(client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
  Hellohttp4sRoutes.helloWorldRoutes(helloWorldAlg) <+>
  Hellohttp4sRoutes.jokeRoutes(jokeAlg)
        ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <-
        EmberServerBuilder.default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
