package edu.knowitall.openregex

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import edu.washington.cs.knowitall.logic.Expression.Arg

/** Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
@RunWith(classOf[JUnitRunner])
class LogicTest extends Specification {
  "Some logic expression" should {
    val logic = Logic.compile[Unit]("true & (true | !false)", (s: String) => s.toLowerCase match {
      case "true" => new Arg.Value(true): Arg[Unit]
      case "false" => new Arg.Value(false): Arg[Unit]
    })

    "be true" in {
      logic.apply() must beTrue
    }
  }
}