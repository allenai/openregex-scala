# OpenRegex-scala

OpenRegex is written by Michael Schmitz at the Turing Center
<http://turing.cs.washington.edu/>.  It is licensed under the Lesser GPL.
Please see the LICENSE file for more details.

OpenRegex-scala provides scala bindings against
[OpenRegex](http://github.com/knowitall/openregex).  The Scala bindings use
Scala collections as well as providing a more familiar API for Scala
users.

OpenRegex-scala also includes a [`PatternParser`
implementation](https://github.com/allenai/openregex-scala/blob/master/src/main/scala/edu/knowitall/openregex/example/PatternParsers.scala)
that uses reflection to run over any class out of the box.  Any public field or
0-argument method may be used to match against a string.  There is an exact
string matcher (single quotes) and a regular expression matcher (forward
slashes).  Multiple aspects of a class can be matched against and combined with
a logical expression.

```scala
    case class Point(x: Int, y: Int)
    val parser = PatternParsers.reflectionWithLogic[Point]
    val regex = parser("<x='1' | x='2'> <x='3' & y='3'>")
    regex.matches(Seq(Point(1, 1), Point(3, 3)))
```

Using regular expressions, the pattern could have been expressed equivalently.

```scala
    val regex = parser("<x=/[12]/> <x='3' & y='3'>")
```
