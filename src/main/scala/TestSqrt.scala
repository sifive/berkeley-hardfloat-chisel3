
/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2017 SiFive, Inc.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of SiFive nor the names of its contributors may
    be used to endorse or promote products derived from this software without
    specific prior written permission.

THIS SOFTWARE IS PROVIDED BY SIFIVE AND CONTRIBUTORS "AS IS", AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE
DISCLAIMED.  IN NO EVENT SHALL SIFIVE OR CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

=============================================================================*/

package hardfloat

import Chisel._
import chisel3.dontTouch

class TestSqrt extends Module
{
  val expWidth = 8
  val sigWidth = 24

  val io = new Bundle {
    val inp = UInt(INPUT, sigWidth + expWidth)
    val outRec = UInt(OUTPUT, sigWidth + expWidth + 1)
    val out = UInt(OUTPUT, sigWidth + expWidth)
  }
  val sq = Module (new DivSqrtRecFN_small(expWidth, sigWidth, 0))
  sq.io.inValid := Bool(true)
  sq.io.sqrtOp := Bool(true)
  sq.io.a := recFNFromFN(expWidth,sigWidth,io.inp)  //recFNFromFN(expWidth,sigWidth,"b00_1110_1010".U)
  val inp = fNFromRecFN(expWidth,sigWidth,sq.io.a)
  sq.io.roundingMode := UInt(0)
  sq.io.detectTininess := UInt(0)
  

  io.outRec := sq.io.out
  io.out := fNFromRecFN(expWidth, sigWidth, io.outRec)

  dontTouch(inp)
  dontTouch(sq.io.sqrtOp)
  dontTouch(sq.io.inValid)
  dontTouch(sq.io.outValid_sqrt)
}
