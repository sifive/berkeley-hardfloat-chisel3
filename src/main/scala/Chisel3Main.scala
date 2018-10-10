
package hardfloat


object Chisel3Main extends App {
  val useString = s"""Use: sbt "runMain ${this.getClass.getName} CONFIG [Chisel args...]"""
  require(args.size >= 1, s"There must be at least 1 argument!\n  $useString")
  val config = args.head
  val chiselArgs = args.tail

  val gen = config match {
    case "RecF16ToF16" => () => new Equiv_RecF16ToF16
    case "RecF32ToF32" => () => new Equiv_RecF32ToF32
    case "RecF64ToF64" => () => new Equiv_RecF64ToF64
    case "F16ToRecF16" => () => new Equiv_F16ToRecF16
    case "F32ToRecF32" => () => new Equiv_F32ToRecF32
    case "F64ToRecF64" => () => new Equiv_F64ToRecF64
    case "F16ToF32" => () => new Equiv_F16ToF32
    case "F16ToF64" => () => new Equiv_F16ToF64
    case "F32ToF64" => () => new Equiv_F32ToF64
    case "F32ToF16" => () => new Equiv_F32ToF16
    case "F64ToF16" => () => new Equiv_F64ToF16
    case "F64ToF32" => () => new Equiv_F64ToF32
    case "F32ToF16" => () => new Equiv_F32ToF16
    case "F64ToF16" => () => new Equiv_F64ToF16
    case "RecF32ToRecF64" => () => new Equiv_RecF32ToRecF64
    case "RecF64ToRecF32" => () => new Equiv_RecF64ToRecF32
    case "RecF16ToRecF64" => () => new Equiv_RecF16ToRecF64
    case "RecF16ToRecF32" => () => new Equiv_RecF16ToRecF32
    case "RecF32ToRecF16" => () => new Equiv_RecF32ToRecF16
    case "RecF64ToRecF16" => () => new Equiv_RecF64ToRecF16
    case "MulAddRecF16" => () => new Equiv_MulAddRecF16
    case "MulAddRecF32" => () => new Equiv_MulAddRecF32
    case "MulAddRecF64" => () => new Equiv_MulAddRecF64
    case "MulAddF16" => () => new Equiv_MulAddF16
    case "MulAddF32" => () => new Equiv_MulAddF32
    case "MulAddF64" => () => new Equiv_MulAddF64
    case "I32ToRecF16" => () => new Equiv_I32ToRecF16
    case "I64ToRecF16" => () => new Equiv_I64ToRecF16
    case "I32ToRecF32" => () => new Equiv_I32ToRecF32
    case "I32ToRecF64" => () => new Equiv_I32ToRecF64
    case "I64ToRecF64" => () => new Equiv_I64ToRecF64
    case "I64ToRecF32" => () => new Equiv_I64ToRecF32
    case "I32ToF16" => () => new Equiv_I32ToF16
    case "I64ToF16" => () => new Equiv_I64ToF16
    case "I32ToF32" => () => new Equiv_I32ToF32
    case "I32ToF64" => () => new Equiv_I32ToF64
    case "I64ToF64" => () => new Equiv_I64ToF64
    case "I64ToF32" => () => new Equiv_I64ToF32
    case "RecF16ToI32" => () => new Equiv_RecF16ToI64
    case "RecF16ToI64" => () => new Equiv_RecF16ToI64
    case "RecF32ToI32" => () => new Equiv_RecF32ToI32
    case "RecF32ToI64" => () => new Equiv_RecF32ToI64
    case "RecF64ToI32" => () => new Equiv_RecF64ToI32
    case "RecF64ToI64" => () => new Equiv_RecF64ToI64
    case "F16ToI64" => () => new Equiv_F16ToI64
    case "F16ToI32" => () => new Equiv_F16ToI32
    case "F32ToI32" => () => new Equiv_F32ToI32
    case "F32ToI64" => () => new Equiv_F32ToI64
    case "F64ToI32" => () => new Equiv_F64ToI32
    case "F64ToI64" => () => new Equiv_F64ToI64
//    case "Bug_F64ToF32" => () => new Bug_F64ToF32
    case "CompareF16" => () => new Equiv_CompareF16
    case "CompareF32" => () => new Equiv_CompareF32
    case "CompareF64" => () => new Equiv_CompareF64
    case "CompareRecF16" => () => new Equiv_CompareRecF16
    case "CompareRecF32" => () => new Equiv_CompareRecF32
    case "CompareRecF64" => () => new Equiv_CompareRecF64
    case "DivF16" => () => new Equiv_DivF16
    case "DivF32" => () => new Equiv_DivF32
    case "DivF64" => () => new Equiv_DivF64
    case "TestSqrt" => () => new TestSqrt
    case c =>
      val msg = s"Config $c not found!\n  $useString"
      throw new java.lang.IllegalArgumentException(msg)
  }
  Chisel.Driver.execute(args, gen)
}
