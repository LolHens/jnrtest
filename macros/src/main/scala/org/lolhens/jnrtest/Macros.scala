package org.lolhens.jnrtest

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
  * Created by pierr on 01.06.2017.
  */
object Macros {
  def hello: Unit = macro helloImpl

  def helloImpl(c: blackbox.Context): c.Expr[Unit] = {
    import c.universe._
    val test = q"""("println")"""
    println(test)
    c.Expr(
      q"""trait User32 {
           def OpenClipboard(hWndNewOwner: Long): Int
           def GetClipboardData(format: Int): String
           def CloseClipboard(): Int
      }
         val user32 = jnr.ffi.LibraryLoader.create(classOf[User32]).load("user32")
         user32.OpenClipboard(0)
         println(user32.GetClipboardData(1))
         user32.CloseClipboard()
         println("test macro2!")""")
  }
}
