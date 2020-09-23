
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

class Equiv_RecFNToFN(expWidth: Int, sigWidth: Int) extends Module
{
    val io = new Bundle {
        val in = Bits(INPUT, expWidth + sigWidth + 1)
        val out = Bits(OUTPUT, expWidth + sigWidth)
        val isGoodRecFN = Bits(OUTPUT, 1)
    }

    io.out := fNFromRecFN(expWidth, sigWidth, io.in)
    io.isGoodRecFN := isGoodRecFN(expWidth, sigWidth, io.in)
}

class Equiv_RecF16ToF16 extends Equiv_RecFNToFN(5, 11)
class Equiv_RecF32ToF32 extends Equiv_RecFNToFN(8, 24)
class Equiv_RecF64ToF64 extends Equiv_RecFNToFN(11, 53)

class Equiv_FNToRecFN(expWidth: Int, sigWidth:Int) extends Module 
{
    val io = new Bundle {
      val in = Bits(INPUT, expWidth + sigWidth)
      val out = Bits(OUTPUT, expWidth + sigWidth + 1)
      val isGoodRecFN = Bits(OUTPUT, 1)
    }

    io.out := recFNFromFN(expWidth, sigWidth, io.in)
    io.isGoodRecFN := isGoodRecFN(expWidth, sigWidth, io.out)
}

class Equiv_F16ToRecF16 extends Equiv_FNToRecFN(5, 11)
class Equiv_F32ToRecF32 extends Equiv_FNToRecFN(8, 24)
class Equiv_F64ToRecF64 extends Equiv_FNToRecFN(11, 53)

