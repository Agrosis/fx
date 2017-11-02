package com.jantox.joker

object Id {

  type Id[A] = A

  implicit object IdFunctor extends Functor[Id] {

    override def map[A, B](f: (A) => B)(a: Id[A]): Id[B] = {
      f(a)
    }

  }

}
