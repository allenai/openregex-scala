package edu.knowitall.openregex

import scala.collection.JavaConverters._
import com.google.common.base.{ Function => GuavaFunction }
import scala.collection.immutable.Range
import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.regex.RegularExpression
import edu.washington.cs.knowitall.regex.RegularExpressionParser
import edu.washington.cs.knowitall.regex.{ Match => JavaMatch }
import edu.washington.cs.knowitall.regex.Expression

case class Pattern[E](val regex: RegularExpression[E]) {
  def apply(tokens: Seq[E]): Boolean = this.matches(tokens)

  def matches(tokens: Seq[E]): Boolean = regex.matches(tokens.asJava)

  def `match`(tokens: Seq[E]): Option[Pattern.Match[E]] = {
    Option(regex.`match`(tokens.asJava)) map Pattern.Match.fromJava
  }

  def find(tokens: Seq[E], start: Int = 0): Option[Pattern.Match[E]] = {
    Option(regex.find(tokens.asJava, start)) map Pattern.Match.fromJava
  }

  def findAll(tokens: Seq[E]): Seq[Pattern.Match[E]] = {
    regex.findAll(tokens.asJava).asScala map Pattern.Match.fromJava
  }

  def lookingAt(tokens: Seq[E], start: Int = 0): Pattern.Match[E] = {
    Pattern.Match.fromJava(regex.lookingAt(tokens.asJava, start))
  }

  def unapplySeq(tokens: Seq[E]): Option[List[String]] = {
    // drop group 0--the whole match
    this.`match`(tokens).map(_.groups.map(_.text).toList.drop(1))
  }
}

case class PatternParser[E](val parser: RegularExpressionParser[E]) {
  def apply(string: String) = Pattern(parser(string))
}

object Pattern {
  private def base[E](string: String, lambda: E=>Boolean) = {
    new BaseExpression[E](string) {
      override def apply(e: E) = lambda(e)
    }
  }

  def compile[E](string: String, factoryF: String=>(E=>Boolean)): Pattern[E] = {
    parser(factoryF).apply(string)
  }

  def parser[E](factoryF: String=>(E=>Boolean)): PatternParser[E] = {
    val parser = new RegularExpressionParser[E]() {
      override def factory(string: String): BaseExpression[E] = base(string, factoryF(string))
    }
    new PatternParser(parser)
  }

  def parser[E](factoryF: String=>(E=>Boolean), readToken: String=>String): PatternParser[E] = {
    val parser = new RegularExpressionParser[E]() {
      override def factory(string: String): BaseExpression[E] = base(string, factoryF(string))
      override def readToken(string: String): String = readToken(string)
    }
    new PatternParser(parser)
  }

  case class Match[E](tokens: Seq[E], pairs: Seq[Group[E]], range: Range) {
    def text = tokens.mkString(" ")

    def groups = pairs.filter(p =>
      p.expr.isInstanceOf[Expression.MatchingGroup[_]] &&
      !(p.expr.isInstanceOf[Expression.NonMatchingGroup[_]]))

    def group(name: String) = this.groups.find {
      _.expr match {
        case group: Expression.NamedGroup[_] => group.name == name
        case _ => false
      }
    }
  }
  object Match {
    def fromJava[E](m: JavaMatch[E]) = {
      Match(m.tokens.asScala, m.pairs.asScala.map(Group.fromJava(_)), Range(m.startIndex, m.endIndex + 1))
    }
  }

  case class Group[E](expr: Expression[E], tokens: Seq[E], interval: Range) {
    def text = tokens.mkString(" ")
  }
  object Group {
    def fromJava[E](group: JavaMatch.Group[E]) = {
      Group(group.expr, group.tokens.asScala, Range(group.startIndex, group.endIndex + 1))
    }
  }
}
