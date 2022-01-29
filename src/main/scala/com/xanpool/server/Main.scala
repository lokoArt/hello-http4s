package com.xanpool.server

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  def run:IO[Unit] = Hellohttp4sServer.run
}
