package me.scripts

import java.io.File
import java.nio.file.Files

trait Action {
  def act(src: File, dest: File): Unit
}

object Action {
  object COPY extends Action {
    override def act(src: File, dest: File): Unit =
      Files.copy(src.toPath, dest.toPath)
  }
  object MOVE extends Action {
    override def act(src: File, dest: File): Unit =
      Files.move(src.toPath, dest.toPath)
  }
}