
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

class Equiv_CompareFN(expWidth: Int, sigWidth: Int) extends Module
{
    val io = new Bundle {
        val a = Bits(INPUT, expWidth + sigWidth)
        val b = Bits(INPUT, expWidth + sigWidth)
        val lt = Bool(OUTPUT)
        val gt = Bool(OUTPUT)
        val eq = Bool(OUTPUT)
        val exceptionFlags = Bits(OUTPUT, 5)
    }

    val compareRecFN = Module(new CompareRecFN(expWidth, sigWidth))
    compareRecFN.io.a := recFNFromFN(expWidth, sigWidth, io.a)
    compareRecFN.io.b := recFNFromFN(expWidth, sigWidth, io.b)
    compareRecFN.io.signaling := Bool(true)

    io.lt := compareRecFN.io.lt
    io.gt := compareRecFN.io.gt
    io.eq := compareRecFN.io.eq
    io.exceptionFlags := compareRecFN.io.exceptionFlags
}

class Equiv_CompareF16 extends Equiv_CompareFN(5, 11)
class Equiv_CompareF32 extends Equiv_CompareFN(8, 24)
class Equiv_CompareF64 extends Equiv_CompareFN(11, 53)

