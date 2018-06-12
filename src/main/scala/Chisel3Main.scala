
package hardfloat


object Chisel3Main extends App {
  val useString = s"""Use: sbt "runMain ${this.getClass.getName} CONFIG [Chisel args...]"""
  require(args.size >= 1, s"There must be at least 1 argument!\n  $useString")
  val config = args.head
  val chiselArgs = args.tail

  val gen = config match {
    case "f16FromRecF16" => () => new ValExec_f16FromRecF16
    case "f32FromRecF32" => () => new ValExec_f32FromRecF32
    case "f64FromRecF64" => () => new ValExec_f64FromRecF64
    case "UI32ToRecF16" => () => new ValExec_UI32ToRecF16
    case "UI32ToRecF32" => () => new ValExec_UI32ToRecF32
    case "UI32ToRecF64" => () => new ValExec_UI32ToRecF64
    case "UI64ToRecF16" => () => new ValExec_UI64ToRecF16
    case "UI64ToRecF32" => () => new ValExec_UI64ToRecF32
    case "UI64ToRecF64" => () => new ValExec_UI64ToRecF64
    case "I32ToRecF16" => () => new ValExec_I32ToRecF16
    case "I32ToRecF32" => () => new ValExec_I32ToRecF32
    case "I32ToRecF64" => () => new ValExec_I32ToRecF64
    case "I64ToRecF16" => () => new ValExec_I64ToRecF16
    case "I64ToRecF32" => () => new ValExec_I64ToRecF32
    case "I64ToRecF64" => () => new ValExec_I64ToRecF64
    case "RecF16ToUI32" => () => new ValExec_RecF16ToUI32
    case "RecF16ToUI64" => () => new ValExec_RecF16ToUI64
    case "RecF32ToUI32" => () => new ValExec_RecF32ToUI32
    case "RecF32ToUI64" => () => new ValExec_RecF32ToUI64
    case "RecF64ToUI32" => () => new ValExec_RecF64ToUI32
    case "RecF64ToUI64" => () => new ValExec_RecF64ToUI64
    case "RecF16ToI32" => () => new ValExec_RecF16ToI32
    case "RecF16ToI64" => () => new ValExec_RecF16ToI64
    case "RecF32ToI32" => () => new ValExec_RecF32ToI32
    case "RecF32ToI64" => () => new ValExec_RecF32ToI64
    case "RecF64ToI32" => () => new ValExec_RecF64ToI32
    case "RecF64ToI64" => () => new ValExec_RecF64ToI64
    case "RecF16ToRecF32" => () => new ValExec_RecF16ToRecF32
    case "RecF16ToRecF64" => () => new ValExec_RecF16ToRecF64
    case "RecF32ToRecF16" => () => new ValExec_RecF32ToRecF16
    case "RecF32ToRecF64" => () => new ValExec_RecF32ToRecF64
    case "RecF64ToRecF16" => () => new ValExec_RecF64ToRecF16
    case "RecF64ToRecF32" => () => new ValExec_RecF64ToRecF32
    case "MulAddRecF16_add" => () => new ValExec_MulAddRecF16_add
    case "MulAddRecF16_mul" => () => new ValExec_MulAddRecF16_mul
    case "MulAddRecF16" => () => new ValExec_MulAddRecF16
    case "MulAddRecF32_add" => () => new ValExec_MulAddRecF32_add
    case "MulAddRecF32_mul" => () => new ValExec_MulAddRecF32_mul
    case "MulAddRecF32" => () => new ValExec_MulAddRecF32
    case "MulAddRecF64_add" => () => new ValExec_MulAddRecF64_add
    case "MulAddRecF64_mul" => () => new ValExec_MulAddRecF64_mul
    case "MulAddRecF64" => () => new ValExec_MulAddRecF64
    case "DivSqrtRecF16_small_div" => () => new ValExec_DivSqrtRecF16_small_div
    case "DivSqrtRecF16_small_sqrt" => () => new ValExec_DivSqrtRecF16_small_sqrt
    case "DivSqrtRecF32_small_div" => () => new ValExec_DivSqrtRecF32_small_div
    case "DivSqrtRecF32_small_sqrt" => () => new ValExec_DivSqrtRecF32_small_sqrt
    case "DivSqrtRecF64_small_div" => () => new ValExec_DivSqrtRecF64_small_div
    case "DivSqrtRecF64_small_sqrt" => () => new ValExec_DivSqrtRecF64_small_sqrt
    case "DivSqrtRecF64_div" => () => new ValExec_DivSqrtRecF64_div
    case "DivSqrtRecF64_sqrt" => () => new ValExec_DivSqrtRecF64_sqrt
    case "CompareRecF16_lt" => () => new ValExec_CompareRecF16_lt
    case "CompareRecF16_le" => () => new ValExec_CompareRecF16_le
    case "CompareRecF16_eq" => () => new ValExec_CompareRecF16_eq
    case "CompareRecF32_lt" => () => new ValExec_CompareRecF32_lt
    case "CompareRecF32_le" => () => new ValExec_CompareRecF32_le
    case "CompareRecF32_eq" => () => new ValExec_CompareRecF32_eq
    case "CompareRecF64_lt" => () => new ValExec_CompareRecF64_lt
    case "CompareRecF64_le" => () => new ValExec_CompareRecF64_le
    case "CompareRecF64_eq" => () => new ValExec_CompareRecF64_eq
    case c =>
      val msg = s"Config $c not found!\n  $useString"
      throw new java.lang.IllegalArgumentException(msg)
  }
  Chisel.Driver.execute(args, gen)
}
