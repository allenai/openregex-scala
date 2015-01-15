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
  val logic = Logic.compile[Unit]("true & (true | !false)", (s: String) => s.toLowerCase match {
    case "true" => (Unit) => true
    case "false" => (Unit) => false
  })

  "Some logic expression" should {
    "be true" in {
      logic.apply() must beTrue
    }
  }
}
