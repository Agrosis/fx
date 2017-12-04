package fx.free

import fx.{Functor, Monad}

sealed trait Free[F[_], A] {

  def map[B](f: A => B)(implicit F: Functor[F]): Free[F, B] = {
    Free.freeFunctor(F).map[A, B](f)(this)
  }

  def flatMap[B](f: A => Free[F, B])(implicit F: Functor[F]): Free[F, B] = {
    Free.freeMonad(F).flatMap[A, B](this)(f)
  }

}

final case class Suspend[F[_], A](f: F[Free[F, A]]) extends Free[F, A]

final case class Pure[F[_], A](a: A) extends Free[F, A]

object Free {

//  def liftF[F[_], A](fa: F[A]): Free[F, A] = {
//    Suspend()
//  }

  implicit def freeFunctor[F[_]: Functor] = new Functor[Free[F, ?]] {
    def map[A, B](f: A => B)(fa: Free[F, A]): Free[F, B] = fa match {
      case Suspend(wrapped) => Suspend(implicitly[Functor[F]].map((freeA: Free[F, A]) => map(f)(freeA))(wrapped))
      case Pure(a) => Pure(f(a))
    }
  }

  implicit def freeMonad[F[_]: Functor] = new Monad[Free[F, ?]] {
    override def point[A](a: A): Free[F, A] = Pure(a)

    override def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma match {
      case Suspend(wrapped) => Suspend(implicitly[Functor[F]].map((freeA: Free[F, A]) => flatMap(freeA)(f))(wrapped))
      case Pure(a) => f(a)
    }
  }

}
