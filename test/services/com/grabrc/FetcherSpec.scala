package services.com.grabrc

import org.specs2.mutable._
import services.com.grabrc._
import play.api.test._
import play.api.test.Helpers._
import com.grabrc.helpers.GithubTest

/**
 * @author Louis
 *
 */

class FetcherSpec extends Specification with GithubTest {

  "The FetcherFactory" should {
      "retrieve BashFetcher for the keyword bash" in { FetcherFactory.getFetcher("bash") must haveClass[BashFetcher] }
      "map foobarrandom to the generic, catch-all Fetcher" in {
        FetcherFactory.getFetcher("foobarrandom") must haveClass[GenericFetcher]
      }
  }

  "When retrieving files, the GenericFetcher" should {
    "fetch the test .vimrc file from Github" in {
      FetcherFactory.getFetcher("vim").fetchContent(USERNAME, ".vimrc", List[String]() ) mustEqual Some(TESTSTRING)
    }
    "return None if the file does not exist" in {
      FetcherFactory.getFetcher("vim").fetchContent(USERNAME, "FILE_NOT_FOUND", List[String]() ) mustEqual None
    }
  }
}
