package services.com.grabrc

import java.net.{URLConnection, URL}
import java.io.InputStream
import java.util.Scanner

/**
 * @author Louis
 *
 */

class GenericFetcher {
  val REPONAME = "grabrc-repo"

  /**
   * Basic processing of arguments for contents.
   * Delegates to [[services.com.grabrc.GenericFetcher#argsAction]]
   * @param content Response content
   * @param args Arguments to the RESTful interface
   */
  private def processArgs(content : String, args : List[String]) =
      this.argsAction(content, args) match {
        case "" => "# No content found for arguments: %s" format args.mkString(" ")
        case result => result
      }

  /**
   * Applies arguments
   * @param content
   * @return The result of processing by a subclass or instance of [[services.com.grabrc.GenericFetcher]]
   */
  protected def argsAction(content : String, args : List[String]) =
    content

  /**
   * Fetches a top level file and handles any argument
   * __Example__: localhost:8080/.bash_profile/aliases
   * retrieves bash_profile with "aliases" as an argument
   * @param username Github username. First argument of route.
   * @param filename Name of file
   * @param argv
   * @return `Some(content)` if file was found. `None` if error or file not found.
   */
  def fetchContent(username : String, filename : String, argv : List[String]) : Option[String] = {
    val url = createFileFetchUrl(username, filename)

    try {
      // Read plaintext response from URL and delegate to subclasses
      val is : InputStream = new URL(url).openStream
      val rawResult = new Scanner(is).useDelimiter("\\A").next
      Some(this.processArgs(rawResult, argv))
    }
    catch {
      case(e : Exception) => None
    }
  }

  /**
   * Contacts the Github server for the archive and forwards the response
   * back to the controller
   * @param username
   * @param compressionType
   * @return A Java URLConnection object
   */
  def fetchRepo(username : String, compressionType : String) : Option[URLConnection]  = {
    def createRepoUrl(username : String, compressionType : String) = {
      val URLPREFIX = "https://nodeload.github.com"
      val zipOrTargz = compressionType match {
        case "zip" => "zipball"
        case "targz" => "tarball"
      }
      "%s/%s/%s/%s/master" format (URLPREFIX, username, REPONAME, zipOrTargz)
    }

    try {
      Some(new URL(createRepoUrl(username, compressionType)).openConnection)
    }
    catch { case e : Exception => None }

  }
  /**
    * Appends strings to create a github.com URL pointing to a raw Github file
    * @param username Github username
    * @param args A list of Strings to append to the end of the URL with "/" between them
    * @return The URL String
    */
    private def createFileFetchUrl(username : String, filename : String) = {
      // Avoid Github API, as it provides a much more complex mechanism
      // for accessing blobs -- requires iteration through SHAs

      val URLPREFIX = "https://raw.github.com"
      "%s/%s/%s/master/%s" format (URLPREFIX, username, REPONAME, filename)
    }
}