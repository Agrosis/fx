package com.jantox.joker

trait Monad[M[_]] extends Functor[M] {
  def returnM[A](a: A): M[A]
  def bind[A, B](f: A => M[B])(ma: M[A]): M[B]

  def fmap[A, B](f: A => B)(fa: M[A]): M[B] = {
    bind[A, B](a => returnM(f(a)))(fa)
  }
}
