
/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018 The Regents of
the University of California.  All rights reserved.

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

object consts {
    /*------------------------------------------------------------------------
    | For rounding to integer values, rounding mode 'odd' rounds to minimum
    | magnitude instead, same as 'minMag'.
    *------------------------------------------------------------------------*/
    def round_near_even   = UInt("b000", 3)
    def round_minMag      = UInt("b001", 3)
    def round_min         = UInt("b010", 3)
    def round_max         = UInt("b011", 3)
    def round_near_maxMag = UInt("b100", 3)
    def round_odd         = UInt("b110", 3)
    /*------------------------------------------------------------------------
    *------------------------------------------------------------------------*/
    def tininess_beforeRounding = UInt(0, 1)
    def tininess_afterRounding  = UInt(1, 1)
    /*------------------------------------------------------------------------
    *------------------------------------------------------------------------*/
    def flRoundOpt_sigMSBitAlwaysZero  = 1
    def flRoundOpt_subnormsAlwaysExact = 2
    def flRoundOpt_neverUnderflows     = 4
    def flRoundOpt_neverOverflows      = 8
}

class RawFloat(val expWidth: Int, val sigWidth: Int) extends Bundle
{
    val isNaN  = Bool()              // overrides all other fields
    val isInf  = Bool()              // overrides 'isZero', 'sExp', and 'sig'
    val isZero = Bool()              // overrides 'sExp' and 'sig'
    val sign   = Bool()
    val sExp = SInt(width = expWidth + 2)
    val sig  = UInt(width = sigWidth + 1)   // 2 m.s. bits cannot both be 0

    override def cloneType =
        new RawFloat(expWidth, sigWidth).asInstanceOf[this.type]
}

//*** CHANGE THIS INTO A '.isSigNaN' METHOD OF THE 'RawFloat' CLASS:
object isSigNaNRawFloat
{
    def apply(in: RawFloat): Bool = in.isNaN && !in.sig(in.sigWidth - 2)
}

object isGoodRecFN 
{
    def apply(expWidth: Int, sigWidth: Int, in: Bits) = 
    { 
        val exp = in(expWidth + sigWidth - 1, sigWidth - 1)
        val exp3 = exp(expWidth, expWidth - 2)
        val sig = in(sigWidth - 2, 0)
        val isZeroGood = (exp3 =/= UInt(0) || sig === UInt(0))
        val emin = UInt(BigInt(1 << (expWidth - 1)) + 2)
        val isBadExp = (exp3 =/= UInt(0)) && (exp < (emin - UInt(sigWidth - 1)))
        val numZeros = emin - exp
        val isSubnormal = exp < emin && exp >= emin - UInt(sigWidth - 1)
        val isSubnormalGood = !isSubnormal || Mux(numZeros === UInt(sigWidth - 1), sig === UInt(0), countLeadingZeros(Reverse(sig)) >= numZeros)
        val isGoodNaN = (exp3 =/= UInt(7) || sig =/= UInt(0))

        isGoodNaN && isZeroGood && isSubnormalGood && !isBadExp

    }
}

object equivRecFN
{
    def apply(expWidth: Int, sigWidth: Int, a: UInt, b: UInt) =
    {
        val top4A = a(expWidth + sigWidth, expWidth + sigWidth - 3)
        val top4B = b(expWidth + sigWidth, expWidth + sigWidth - 3)
        Mux((top4A(2, 0) === UInt(0)) || (top4A(2, 0) === UInt(7)),
            (top4A === top4B) && (a(sigWidth - 2, 0) === b(sigWidth - 2, 0)),
            Mux((top4A(2, 0) === UInt(6)), (top4A === top4B), (a === b))
        )
    }
}
