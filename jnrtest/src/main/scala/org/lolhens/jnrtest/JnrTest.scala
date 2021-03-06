package org.lolhens.jnrtest

import java.nio.charset.StandardCharsets

import jnr.ffi.types.{pid_t, size_t}
import jnr.ffi.{LibraryLoader, Pointer, Runtime}
import scodec.bits.ByteVector

/**
  * Created by pierr on 30.05.2017.
  */
object JnrTest {
  def main(args: Array[String]): Unit = {
    //val libc = LibraryLoader.create(classOf[LibC]).load("C:\\Program Files\\cygwin64\\bin\\cygwin1.dll")
    //System.out.println(" pid=" + libc.getpid + " parent pid=" + libc.getppid + " login=" + libc.getlogin)

    val ll = LibraryLoader.create(classOf[User32])
    /*println({
      val field = classOf[LibraryLoader[Any]].getDeclaredField("typeMapperBuilder")
      field.setAccessible(true)
      val builder = field.get(ll).asInstanceOf[TypeMapper.Builder]
      println(builder)
      val b = classOf[TypeMapper.Builder].getDeclaredField("toNativeConverterMap")
      b.setAccessible(true)
      println(b.get(builder).asInstanceOf[util.Map[Class[_], ToNativeConverter[_, _]]].)
    })*/
    ll.failImmediately()
    val user32 = ll.load("user32")
    /*.map(classOf[Boolean], new AbstractDataConverter[Boolean, Int] {
    override def fromNative(n: Int, fromNativeContext: FromNativeContext): Boolean = if (n == 0) false else true
    override def nativeType(): Class[Int] = classOf[Int]
    override def toNative(j: Boolean, toNativeContext: ToNativeContext): Int = if (j) 1 else 0
  }).load("user32")*/
    val runtime = Runtime.getRuntime(user32)

    val kernel32 = LibraryLoader.create(classOf[Kernel32]).load("kernel32")
    val string = "abcdefg"

    val hmem = kernel32.allocateGlobal(ByteVector.encodeAscii(string).toOption.get)

    /*val hmem = kernel32.GlobalAlloc(GMEM_MOVEABLE, string.length + 1)
    val ptr = kernel32.GlobalLock(hmem)

    ptr.putString(0, string, string.length, StandardCharsets.US_ASCII)

    kernel32.GlobalUnlock(hmem)
*/
    /*println(ptr.getByte(0))
    println(ptr.getByte(1))
    println(ptr.putByte(0, 36))
    println(ptr.getByte(0))
    println(ptr.getByte(1))*/
    //println(ptr.address())


    println(user32.OpenClipboard(null)) //Pointer.wrap(runtime, 0)))
    //println(user32.EmptyClipboard())
    println("clipboard:")
    println(user32.GetClipboardData(1))
    //println(user32.IsClipboardFormatAvailable(1))
    println(user32.EmptyClipboard())

    println(user32.SetClipboardData(1, hmem)) //ptr.address() /*WHAT?!?*/))// Pointer.wrap(runtime, ByteBuffer.wrap("asdf".getBytes))))

    println(user32.CloseClipboard())

    println(user32.OpenClipboard(null)) //Pointer.wrap(runtime, 0)))
    //println(user32.EmptyClipboard())
    println("clipboard:")
    println(user32.GetClipboardData(1))
    println(user32.CloseClipboard())
    //println(kernel32.GlobalFree(hmem))

  }

  trait LibC {
    @pid_t def getpid: Long

    @pid_t def getppid: Long

    def getlogin: String
  }

  case class Handle(address: Long) extends AnyVal

  val GMEM_MOVEABLE = 0x02
  val GMEM_ZEROINIT = 0x40

  trait Kernel32 {
    def allocateGlobal(data: ByteVector): Handle = {
      val handle = GlobalAlloc(GMEM_MOVEABLE, data.size + 1)
      val pointer = GlobalLock(handle)
      pointer.put(0L, data.toArray, 0, data.size.toInt)
      GlobalUnlock(handle)
      handle
    }

    def GetLastError(): String

    def GlobalAlloc(flags: Int, size: Long@size_t): Handle

    def GlobalLock(handle: Handle): Pointer

    def GlobalUnlock(handle: Handle): Int

    def GlobalFree(handle: Handle): Pointer
  }

  trait User32 {
    def OpenClipboard(hWndNewOwner: Pointer): Int

    //def openClipboard: Boolean = OpenClipboard(null) != 0
    def IsClipboardFormatAvailable(format: Int): Int

    def GetClipboardData(format: Int): String

    def EmptyClipboard(): Int

    def SetClipboardData(format: Int, handle: Handle): Pointer

    //def SetClipboardData(format: Int, string: String): Unit = SetClipboardData(format, )

    def CloseClipboard(): Int
  }

}
