package org.veupathdb.sitesearch.usercomments;

public class CommentUpdaterCli {

  protected static void validateEnv(String[] envVarKeys) {
    final var env = System.getenv();
    String msg = "Error. Missing at least one required environment variable: " + String.join(", ", envVarKeys);
    for (String key : envVarKeys) {
      if (!env.containsKey(key)) {
        System.err.println(msg);
        System.exit(1);
      }
    }
  }

  /**
   * The only supported argument is "doUpdate". Exit with an error on anything else.
   * @return true if doUpdate was provided; false means report only
   */
  protected static boolean parseDoUpdate(String[] args) {
    boolean doUpdate = false;
    for (String arg : args) {
      if (arg.equals("doUpdate")) doUpdate = true;
      else {
        System.err.println("Error. Unrecognized argument '" + arg + "'. The only allowed argument is: doUpdate (update SOLR; else, report only)");
        System.exit(1);
      }
    }
    return doUpdate;
  }
}
