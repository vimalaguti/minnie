package me.scripts

import java.io.File

/**
 * Minnie copies the contents of a directory into smaller directories
 */
object Minnie {

  case class Config(
                     src: File = new File("."),
                     dest: File = new File("."),
                     prefix: String = "",
                     size: Int = 100,
                     action: Action = Action.COPY
                   )

  def main(args: Array[String]): Unit = {
    val parser = new scopt.OptionParser[Config]("scopt") {
      head("Minnie", "0.1")

      opt[File]('s', "src")
        .required()
        .action{ case (x, c) => c.copy(src = x) }
        .text("source directory to split")
        .validate( src =>
                  if (!src.isDirectory) failure(s"src is not a directory ($src)")
                  else if (src.list().length <= 0) failure("src should not be empty")
                  else success
                  )

      opt[File]('d', "dest")
        .optional()
        .validate( dest =>
                   if (dest.exists() && !dest.isDirectory) failure(s"dest should be a directory ($dest)")
                   else if (dest.list().nonEmpty) failure("dest should be empty")
                   else success
                   )
        .valueName("<dest>")
        .action( (x, c) => c.copy(dest = x) )
        .text("an existing path where to create destination folders")

      opt[Int]('s', "size")
        .required()
        .action{case (x, c) => c.copy(size = x)}
        .validate(n => if (n > 0) success else failure("size should be positive"))
        .text("maximum count for <libname>")

      opt[Unit]('c', "copy")
        .optional()
        .action{case (_, c) => c.copy(action = Action.COPY)}
        .text("set to copy the src files into dest")

      opt[Unit]('m', "move")
        .optional()
        .action{case (_, c) => c.copy(action = Action.MOVE)}
        .text("set to move the src files into dest")

      opt[String]("prefix")
        .optional()
        .action{case (p, c) => c.copy(prefix = p)}
        .text("set the prefix name of subdirectories - eg. <prefix>50-60")
      // note("some notes.")
    }

    parser.parse(args, Config()) match {
        case Some(config) => minnie(config)
        case None         => ()
      }
  }

  def minnie(config: Config): Unit = {
    val files = config.src.listFiles().sorted
    for {
      (batch, dirName) <- splitInBatches(config.size, files, config.prefix)
      subDir = createDir(config.dest, dirName)
      (sampleSrc, sampleDst) <- batch.map(src => (src, new File(subDir, src.getName)))
    } {
      config.action.act(sampleSrc, sampleDst)
    }
  }

  def createDir(parent: File, name: String): File = {
    val dest = new File(parent, name)
    dest.mkdir()
    dest
  }

  def splitInBatches(max: Int, l: Array[File], prefix: String): Seq[(Array[File], String)] = {
    (l.indices by max).zip(max until l.length + max by max)
       .map{case (start, end) =>
         val startName = l(math.max(start, 0)).getName.split('.').head
         val endName = l(math.min(end - 1, l.length - 1)).getName.split('.').head
         (
           l.slice(start, end),
           s"$prefix$startName-$endName"
         )
       }
  }

}
