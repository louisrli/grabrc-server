package controllers.com.grabrc

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import com.grabrc.helpers.GithubTest
import play.api.mvc.Results.NotFound

/**
 * @author Louis
 *
 */

class ApplicationControllerSpec extends Specification with GithubTest {
  def defaultFetch (file : String) = {
    Application.getFile(USERNAME, file, null)(FakeRequest())
  }

  "In the file route, the controller" should {

   "retrieve the test .vimrc" in {
      val result = defaultFetch(".vimrc")
      contentAsString(result) mustEqual TESTSTRING
    }

    "sanitize input" in {
      val slashResult = defaultFetch(".v/i/m/r/c")
      val questionResult = defaultFetch("?.v???i?m?rc")
      contentAsString(slashResult) mustEqual TESTSTRING
      contentAsString(questionResult) mustEqual TESTSTRING
    }

    "ignore redundant arguments" in {
      val result = Application.getFile(USERNAME, ".vimrc", "foo/bar/yam/zee")(FakeRequest())
      contentAsString(result) mustEqual TESTSTRING
    }

    "return NotFound when the file doesn't exist" in {
      val nullResult = defaultFetch("EMPTY_FILE")
      status(nullResult) mustEqual(404)
    }
  }

  "In the archive route, the controller" should {
      "get a response header of application/octet-stream for zipfiles" in {
        val result = Application.getRepo(USERNAME, "zip")(FakeRequest())
        contentType(result) must beSome("application/octet-stream")
      }
  }
}
