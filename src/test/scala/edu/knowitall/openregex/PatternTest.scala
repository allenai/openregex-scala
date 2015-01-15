package edu.knowitall.openregex

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.logic.Expression.Arg

/** Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
@RunWith(classOf[JUnitRunner])
class PatternTest extends Specification {
  def deserialize(matcher: String) = {
    (string: String) => string == matcher
  }
  "Basic regular expression tests" should {
    "pass successfully" in {
      val regex = Pattern.compile[String]("<hello>? <goodbye>* <.>", deserialize _)

      regex.matches(List("hello", "goodbye", ".")) must beTrue
      regex.matches(List("hello", "goodbye", "goodbye", ".")) must beTrue
      regex.matches(List("hello", "goodbye", "goodbye")) must beFalse
    }
  }

  "Matching group regular expression tests" should {
    "pass successfully" in {
      val regex = Pattern.compile[String]("(<hello>?) (<foo>:<goodbye>*) <.>", deserialize _)

      val m = regex.find(List("hello", "goodbye", "goodbye", ".")).get
      m.groups(0).text == "hello"
      m.groups(1).text == "goodbye goodbye"
      m.group("foo").get.text == "goodbye goodbye"
    }
  }

  "Pattern extractor works" should {
    "pass successfully" in {
      val regex = Pattern.compile[String]("(<hello>) <no> <way> (<goodbye>)", deserialize _)

      val list = List("hello", "no", "way", "goodbye")

      val result = list match {
        case regex(greeting, farewell) => greeting == "hello" && farewell == "goodbye"
        case _ => false
      }

      result must beTrue
    }
  }
}
