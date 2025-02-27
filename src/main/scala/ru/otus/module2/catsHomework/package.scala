package ru.otus.module2

import cats.Functor
import cats.implicits._
import scala.util.{Failure, Success, Try}

package object catsHomework {

  /** Простое бинарное дерево
    * @tparam A
    */
  sealed trait Tree[+A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  /** Напишите instance Functor для объявленного выше бинарного дерева.
    * Проверьте, что код работает корректно для Branch и Leaf
    */
  implicit lazy val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value)         => Leaf(f(value))
    }
  }

  /** Monad абстракция для последовательной комбинации вычислений в контексте F
    * @tparam F
    */
  trait Monad[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(v => pure(f(v)))
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    def pure[A](v: A): F[A]
  }

  /** MonadError расширяет возможность Monad кроме последовательного применения
    * функций, позволяет обрабатывать ошибки
    * @tparam F
    * @tparam E
    */
  trait MonadError[F[_], E] extends Monad[F] {
    // Поднимаем ошибку в контекст `F`:
    def raiseError[A](e: E): F[A]

    // Обработка ошибки, потенциальное восстановление:
    def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]

    // Обработка ошибок, восстановление от них:
    def handleError[A](fa: F[A])(f: E => A): F[A]

    // Test an instance of `F`,
    // failing if the predicate is not satisfied:
    def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
  }

  /** Напишите instance MonadError для Try
    */
  implicit lazy val tryME: MonadError[Try, Throwable] =
    new MonadError[Try, Throwable] {
      override def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] =
        fa.flatMap(f)

      override def pure[A](v: A): Try[A] =
        Success(v)

      override def raiseError[A](e: Throwable): Try[A] =
        Failure(e)

      override def handleErrorWith[A](fa: Try[A])(
          f: Throwable => Try[A]
      ): Try[A] = fa.recoverWith(f(_))

      override def handleError[A](fa: Try[A])(f: Throwable => A): Try[A] =
        fa.recover(f(_))

      override def ensure[A](fa: Try[A])(e: Throwable)(
          f: A => Boolean
      ): Try[A] =
        fa.filter(f).orElse(Failure(e))
    }

  /** Напишите instance MonadError для Either, где в качестве типа ошибки будет
    * String
    */
  type Eith[A] = Either[String, A]
  implicit val eitherME: MonadError[Eith, String] =
    new MonadError[Eith, String] {
      override def flatMap[A, B](fa: Eith[A])(f: A => Eith[B]): Eith[B] =
        fa.flatMap(f)

      override def pure[A](v: A): Eith[A] =
        Right(v)

      override def raiseError[A](e: String): Eith[A] =
        Left(e)

      override def handleErrorWith[A](fa: Eith[A])(
          f: String => Eith[A]
      ): Eith[A] =
        fa.left.flatMap(f)

      override def handleError[A](fa: Eith[A])(f: String => A): Eith[A] =
        fa.left.flatMap(x => pure(f(x)))

      override def ensure[A](fa: Eith[A])(e: String)(f: A => Boolean): Eith[A] =
        fa.filterOrElse(f, e)
    }
}
