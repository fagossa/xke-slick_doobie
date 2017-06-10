package com.xebia.doobie.query

import com.xebia.doobie.HelloDoobie
import org.scalatest._

class DoobieSpec extends FunSpec with ShouldMatchers {

  describe("Doobie") {

    describe("connections") {

      it("should do a roundtrip and return a result") {
        HelloDoobie.result should be(42)
      }

    }

    describe("Programs") {

      it("should be used as monads") {
        MonadOrFunctor.resultAsMonad should be(67)
      }

      it("should be used as functors") {
        MonadOrFunctor.resultAsFunctor should be(67)
      }

    }
  }

}
