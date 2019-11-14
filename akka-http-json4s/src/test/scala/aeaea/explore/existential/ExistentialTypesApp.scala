package aeaea.explore.existential
/*
The name AEAEA derives from the mythical Greek island of Circe,
where Homer’s Odysseus was trapped without time or history.

A.A.L (Against All Logic)
A.L.A. (All Logic Absent)
 */
object ExistentialTypesApp extends App {

  // https://dzone.com/articles/existential-types-in-scala
  /*
  Existential types are a way of abstracting over types.
  They let you “acknowledge” that there is a type involved without specifying exactly what it is,
  usually because you don’t know what it is and you don’t need that knowledge in the current context.
   */
  {
    def getLength(x: Array[_]): Int = x.length
    println(getLength(Array[String]("foo", "bar")))
  }
  // ex 0:
  object Example0 {
    sealed trait F[A]
    final case class SomeClass[A](a: A) extends F[A]
    // F[A] is the same as
    type G[A] = SomeClass[A]

    SomeClass("hello"): F[String]
    SomeClass(1: Int): F[Int]
    SomeClass(true): G[Boolean]
    // SomeClass("hello"): G[Int]} - won't compile
  }

  // ex 1:
  object Example1 {
    sealed trait F
    final case class SomeClass[A](a: A) extends F
    // F and all these are the same:
    type G = SomeClass[A] forSome { type A }
    type H = SomeClass[_]
    type I = F

    SomeClass("SomeString"): F
    SomeClass(1: Int): G
    SomeClass(User): H
    SomeClass(true): I
  }

  // ex 2:
  object Example2 {
    sealed trait Existential {
      type Inner
      val value: Inner
    }
    final case class PrepareExistential[A](value: A) extends Existential {
      type Inner = A
    }
    PrepareExistential("SomeText"): Existential
    PrepareExistential(1: Int): Existential
    PrepareExistential(User): Existential
  }

  /*
  from Martin Odersky himself:
  “Scala uses the erasure model of generics, just like Java, so we don’t see the type parameters anymore when programs are run.
  We have to do erasure because we need to interoperate with Java.
  But then what happens when we do reflection or want to express what goes on in the VM?
  We need to be able to represent what the JVM does using the types we have in Scala, and existential types let us do that.”
   */

  // ex 3:
  {
    def getLength(x: Array[T] forSome { type T <: CharSequence }) =
      x.foreach(y => println(y.length))
    println(getLength(Array[String]("foo", "bar", "baz")))
    // Notice the additional restriction we have imposed.
    // Here, we wanted to act on a more specific type, but did not care exactly what type it was.
    // The type arguments for an existential type can declare upper and lower bounds just like normal type declarations.
    // By adding bounds, all we have done is restrict the range of T.
    // An Array[Int] is not an Array[T] forSome { type T <: CharSequence }

    // In a generic definition, for a type T, M[T] is a type, but M is not itself a type.
    // M could be List, Array, Class, etc.
    // M[T] forSome { type T; } is the type of all things for which there is some T such that they are of type M[T].
    // So an Array[String] is of this type because we can choose T = String.
  }

  // some random case class to be used in the examples above
  case class User(name: String, age: Int, contact: String)
}
