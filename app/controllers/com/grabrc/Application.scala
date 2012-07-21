package controllers.com.grabrc

import play.api._
import libs.iteratee.Enumerator
import play.api.mvc._
import services.com.grabrc._
import java.net.URL

object Application extends Controller {

  /**
   * Fetches the most basic case -- a single primary file (.emacs, .vimrc)
   * @param username Github username
   * @param filename Filename
   * @param restfulArgs Forward slash delimited list of arguments from route
   * @return
   */
  def getFile(username : String, filename : String, restfulArgs : String) = {
    // Sanitize input for security
    def sanitize(n : String) =
       n.replaceAllLiterally("/", "")
        .replaceAllLiterally("?", "")

    val fetcher = FetcherFactory.getFetcher(filename)

    // Pass the sanitized strings to the Fetcher service
    val argList = restfulArgs match {
      case null => List[String]()
      case s => s.split("/").toList.map(sanitize)
    }

    fetcher.fetchContent(sanitize(username), sanitize(filename), argList) match {
      case Some(content) => Ok(content)
      case None => NotFound("File not found.")
    }
  }

  def getRepo(username : String, compressionType : String) : Result = {
    val fetcher = FetcherFactory.getFetcher("archive")

    fetcher.fetchRepo(username, compressionType) match {
      case Some(conn) =>
        SimpleResult(
          header = ResponseHeader(200, Map(CONTENT_TYPE -> conn.getContentType)),
          body = Enumerator(conn.getContent.toString)
        )
      case None => NotFound("Repository not found.")
    }

  }
}