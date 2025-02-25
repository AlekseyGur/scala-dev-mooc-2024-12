package reader.service

class Monad[T, Src](private val p: Src => (T, Src)) {
  def flatMap[M](f: T => Monad[M, Src]): Monad[M, Src] =
    Monad { src =>
      val (word, rest) = p(src)
      f(word).p(rest)
    }

  def map[M](f: T => M): Monad[M, Src] =
    Monad { src =>
      val (word, rest) = p(src)
      (f(word), rest)
    }

  def parse(src: Src): T = p(src)._1
}

object Monad {
  def apply[T, Src](f: Src => (T, Src)) =
    new Monad[T, Src](f)
}
