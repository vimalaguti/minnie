package me.scripts

import java.io.File
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class MinnieTest extends AnyFlatSpec with Diagrams with Matchers {

  val tmpDir = new File(System.getProperty("java.io.tmpdir"))

  "Minnie" should "create a directory" in {
    val out = Minnie.createDir(tmpDir, "test")
    assert(out.isDirectory)
    out.delete()
  }

  it should "split a list in batches" in {
    val files = (0 until 17)
                      .map(i => new File(s"$i"))
                      .toArray
    val batches = Minnie.splitInBatches(5, files, "")
                        .map(_._1.map(_.getName.toInt))
    val target = (0 until 17).sliding(5, 5)

    assert(batches.zip(target).forall{case (a, b) => a sameElements b})
  }

  it should "assign the range as the folder name" in {
    val files = (0 until 17)
                      .map(i => new File(s"$i.jpg"))
                      .toArray
    val batches = Minnie.splitInBatches(5, files, "abc")
                        .map(_._2)
    val target = (0 until 17).sliding(5, 5).map(c => s"abc${c.head}-${c.last}").toList

    assert(
      batches.zip(target).forall{case (a, b) => a == b}
      )
  }

}
