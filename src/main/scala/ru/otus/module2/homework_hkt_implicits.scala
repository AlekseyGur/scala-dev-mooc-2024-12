package ru.otus.module2

object homework_hkt_implicits {

  trait Bindable[F[_], A] {
    def map[B](f: A => B): F[B]

    def flatMap[B](f: A => F[B]): F[B]
  }

  implicit def im1[A](a: Option[A]): Bindable[Option, A] = new Bindable[Option, A] {
    override def map[B](f: A => B): Option[B] = a.map(f)

    override def flatMap[B](f: A => Option[B]): Option[B] = a.flatMap(x => f(x))
  }

  implicit def im2[A](a: List[A]): Bindable[List, A] = new Bindable[List, A] {
    override def map[B](f: A => B): List[B] = a.map(f)

    override def flatMap[B](f: A => List[B]): List[B] = a.flatMap(x => f(x))
  }

  implicit def im3[A](a: Some[A]): Bindable[Some, A] = new Bindable[Some, A] {
    override def map[B](f: A => B): Some[B] = Some(a.value.asInstanceOf[B])

    override def flatMap[B](f: A => Some[B]): Some[B] = f(a.value).asInstanceOf[Some[B]]
  }

  def tupleF[F[_], A, B](fa: Bindable[F, A], fb: Bindable[F, B]): F[(A, B)] =
    fa.flatMap(a => fb.map(b => (a, b)))

    
  // Применение:
  val r1 = tupleF(List("text"), List(1, 2, 3))
  val r2 = tupleF(Some("text"), Some(3))

  println(r1)
  println(r2)


  // Второй вариант:
  def tupleFi[F[_], A, B](a: F[A], b: F[B])(implicit
      m1: F[A] => Bindable[F, A],
      m2: F[B] => Bindable[F, B]
  ): F[(A, B)] =
    m1(a).flatMap(a => m2(b).map(b => (a, b)))

  val r4 = tupleFi(List("text"), List(1, 2, 3))
  val r5 = tupleFi(Some("text"), Some(3))
  val r6 = tupleFi(Some("text"), Some(3))

  println(r4)
  println(r5)
  println(r6)
}