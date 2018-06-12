
/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017 The Regents of the
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

class Equiv_MulAddRecFN(expWidth: Int, sigWidth: Int, op: UInt) extends Module
{
    val io = new Bundle {
        val a = Bits(INPUT, expWidth + sigWidth)
        val b = Bits(INPUT, expWidth + sigWidth)
        val c = Bits(INPUT, expWidth + sigWidth)
        val roundingMode   = UInt(INPUT, 3)
        val detectTininess = UInt(INPUT, 1)

        val out = Bits(OUTPUT, expWidth + sigWidth + 1)
        val exceptionFlags = Bits(OUTPUT, 5)
    }

    val mulAddRecFN = Module(new MulAddRecFN(expWidth, sigWidth))
    mulAddRecFN.io.op := op
    mulAddRecFN.io.a := io.a
    mulAddRecFN.io.b := io.b
    mulAddRecFN.io.c := io.c
    mulAddRecFN.io.roundingMode   := io.roundingMode
    mulAddRecFN.io.detectTininess := io.detectTininess

    io.out := mulAddRecFN.io.out
    io.exceptionFlags := mulAddRecFN.io.exceptionFlags
}

class Equiv_MulAddRecF16 extends Equiv_MulAddRecFN(5, 11, UInt(0))
class Equiv_MulAddRecF32 extends Equiv_MulAddRecFN(8, 24, UInt(0))
class Equiv_MulAddRecF64 extends Equiv_MulAddRecFN(11, 53, UInt(0))

class Equiv_MulAddFN(expWidth: Int, sigWidth: Int, op: UInt) extends Module
{
    val io = new Bundle {
        val a = Bits(INPUT, expWidth + sigWidth)
        val b = Bits(INPUT, expWidth + sigWidth)
        val c = Bits(INPUT, expWidth + sigWidth)
        val roundingMode   = UInt(INPUT, 3)
        val detectTininess = UInt(INPUT, 1)

        val out = Bits(OUTPUT, expWidth + sigWidth + 1)
        val exceptionFlags = Bits(OUTPUT, 5)
    }

    val mulAddRecFN = Module(new MulAddRecFN(expWidth, sigWidth))
    mulAddRecFN.io.op := op
    mulAddRecFN.io.a := recFNFromFN(expWidth, sigWidth, io.a)
    mulAddRecFN.io.b := recFNFromFN(expWidth, sigWidth, io.b)
    mulAddRecFN.io.c := recFNFromFN(expWidth, sigWidth, io.c)
    mulAddRecFN.io.roundingMode   := io.roundingMode
    mulAddRecFN.io.detectTininess := io.detectTininess

    io.out := fNFromRecFN(expWidth, sigWidth, mulAddRecFN.io.out)
    io.exceptionFlags := mulAddRecFN.io.exceptionFlags
}

class Equiv_MulAddF16 extends Equiv_MulAddFN(5, 11, UInt(0))
class Equiv_MulAddF32 extends Equiv_MulAddFN(8, 24, UInt(0))
class Equiv_MulAddF64 extends Equiv_MulAddFN(11, 53, UInt(0))
