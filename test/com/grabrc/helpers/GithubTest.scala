package com.grabrc.helpers

import org.specs2.mutable.Specification

/**
 * Contains state about basic Github unit tests
 * @author Louis
 *
 */

trait GithubTest extends Specification {
  val USERNAME = "louisrli"
  val TESTSTRING = "# test. I don't use vim."
  val EMPTYLIST = List[String]()
}
