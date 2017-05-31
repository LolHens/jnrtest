package org.lolhens.jnrtest

/**
  * Created by pierr on 31.05.2017.
  */
abstract class NativeInterface(libraryName: String) {
  protected val mapType: Nothing = ???

  protected def native[A](functionName: String): E

  class E {
    def apply(i: Int, s: String): String = ???
  }

  native("asdf")(3, "")

}
