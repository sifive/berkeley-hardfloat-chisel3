
/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016 The Regents of the
University of California.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of the University nor the names of its contributors may
    be used to endorse or promote products derived from this software without
    specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS", AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE
DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

=============================================================================*/

package hardfloat

import Chisel._

class
    Equiv_FNToFN(
        inExpWidth: Int, inSigWidth: Int, outExpWidth: Int, outSigWidth: Int)
    extends Module
{
    val io = new Bundle {
        val in = UInt(INPUT, inExpWidth + inSigWidth)
        val roundingMode   = UInt(INPUT, 3)
        val detectTininess = UInt(INPUT, 1)

        val out = UInt(OUTPUT, outExpWidth + outSigWidth)
        val exceptionFlags = UInt(OUTPUT, 5)
    }

    val recFNToRecFN =
        Module(
            new RecFNToRecFN(inExpWidth, inSigWidth, outExpWidth, outSigWidth))
    recFNToRecFN.io.in := recFNFromFN(inExpWidth, inSigWidth, io.in)
    recFNToRecFN.io.roundingMode   := io.roundingMode
    recFNToRecFN.io.detectTininess := io.detectTininess

    io.out := fNFromRecFN(outExpWidth, outSigWidth, recFNToRecFN.io.out)
    io.exceptionFlags := recFNToRecFN.io.exceptionFlags

}

class Equiv_F16ToF32 extends Equiv_FNToFN(5, 11, 8, 24)
class Equiv_F16ToF64 extends Equiv_FNToFN(5, 11, 11, 53)
class Equiv_F32ToF16 extends Equiv_FNToFN(8, 24, 5, 11)
class Equiv_F32ToF64 extends Equiv_FNToFN(8, 24, 11, 53)
class Equiv_F64ToF16 extends Equiv_FNToFN(11, 53, 5, 11)
class Equiv_F64ToF32 extends Equiv_FNToFN(11, 53, 8, 24)

class
    Equiv_RecFNToRecFN(
        inExpWidth: Int, inSigWidth: Int, outExpWidth: Int, outSigWidth: Int)
    extends Module
{
    val io = new Bundle {
        val in = UInt(INPUT, inExpWidth + inSigWidth + 1)
        val roundingMode   = UInt(INPUT, 3)
        val detectTininess = UInt(INPUT, 1)
        val kami_out = UInt(INPUT, outExpWidth + outSigWidth + 1)

        val out = UInt(OUTPUT, outExpWidth + outSigWidth + 1)
        val exceptionFlags = UInt(OUTPUT, 5)
        val isGoodIn = UInt(OUTPUT, 1)
        val isGoodOut = UInt(OUTPUT, 1)
        val isEquiv = UInt(OUTPUT, 1)
    }

    val recFNToRecFN =
        Module(
            new RecFNToRecFN(inExpWidth, inSigWidth, outExpWidth, outSigWidth))
    recFNToRecFN.io.in := io.in
    recFNToRecFN.io.roundingMode   := io.roundingMode
    recFNToRecFN.io.detectTininess := io.detectTininess

    io.out := recFNToRecFN.io.out
    io.exceptionFlags := recFNToRecFN.io.exceptionFlags

    io.isGoodIn := isGoodRecFN(inExpWidth, inSigWidth, io.in)
    io.isGoodOut := isGoodRecFN(outExpWidth, outSigWidth, io.out)
    io.isEquiv := equivRecFN(outExpWidth, outSigWidth, io.out, io.kami_out)
}

class Equiv_RecF16ToRecF32 extends Equiv_RecFNToRecFN(5, 11, 8, 24)
class Equiv_RecF16ToRecF64 extends Equiv_RecFNToRecFN(5, 11, 11, 53)
class Equiv_RecF32ToRecF16 extends Equiv_RecFNToRecFN(8, 24, 5, 11)
class Equiv_RecF32ToRecF64 extends Equiv_RecFNToRecFN(8, 24, 11, 53)
class Equiv_RecF64ToRecF16 extends Equiv_RecFNToRecFN(11, 53, 5, 11)
class Equiv_RecF64ToRecF32 extends Equiv_RecFNToRecFN(11, 53, 8, 24)

//class Bug_F64ToF32() extends Module
//{
//    val io = new Bundle {
//       val fn64 = UInt(OUTPUT, 64)
//       val recfn64 = UInt(OUTPUT, 65)
//       val roundMode = UInt(OUTPUT, 3)
//       val tiny = UInt(OUTPUT, 1)
//       val fn32 = UInt(OUTPUT, 32)
//       val recfn32 = UInt(OUTPUT, 33)
//       val flags = UInt(OUTPUT, 5)
//    }
//
//    val sth = Module (new Equiv_FNToFN(11, 53, 8, 24))
//    io.fn64 := UInt("b0010111111110000000000000000000000000000000000000000000000000000")
//    io.recfn64 := recFNFromFN(11, 53, io.fn64)
//    io.roundMode := UInt("b001")
//    io.tiny := UInt("b1")
//    sth.io.in := io.fn64
//    sth.io.roundingMode := io.roundMode
//    sth.io.detectTininess := io.tiny
//    io.fn32 := sth.io.out
//    io.recfn32 := recFNFromFN(8, 24, io.fn32)
//    io.flags := sth.io.exceptionFlags
//}
