package services.com.grabrc

/**
 * @author Louis
 *
 */

object FetcherFactory {

  def getFetcher(moduleName : String) : GenericFetcher = {
    moduleName match {
      case "bash" | ".bashrc" | ".bash_profile" => new BashFetcher
      case _ => new GenericFetcher
    }
  }
}
