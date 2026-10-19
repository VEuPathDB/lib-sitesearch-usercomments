package org.veupathdb.sitesearch.usercomments;

import org.gusdb.fgputil.db.platform.SupportedPlatform;
import org.gusdb.fgputil.db.pool.DatabaseInstance;
import org.gusdb.fgputil.db.pool.SimpleDbConfig;

public class ApolloCommentUpdaterCli extends CommentUpdaterCli {
  
  private final static String DB_HOST = "APPDB_HOST";
  private final static String DB_PORT = "APPDB_PORT";
  private final static String DB_NAME = "APPDB_LDAP_CN";
  private final static String DB_LOGIN = "APPDB_LOGIN";
  private final static String DB_PASSWORD = "APPDB_PASSWORD";
  private final static String SOLR_URL = "SOLR_URL";
  private final static String PROJECT_ID = "PROJECT_ID";
 
  public static void main(String[] args) throws Exception {

    String[] envVarKeys = {DB_HOST, DB_PORT, DB_NAME, DB_LOGIN, DB_PASSWORD, SOLR_URL, PROJECT_ID};
    validateEnv(envVarKeys);

    final var env = System.getenv();
    SimpleDbConfig dbConf = SimpleDbConfig.create(
        SupportedPlatform.POSTGRESQL,
        SupportedPlatform.POSTGRESQL.getPlatformInstance().getConnectionUrl(env.get(DB_HOST), Integer.parseInt(env.get(DB_PORT)), env.get(DB_NAME)),
        env.get(DB_LOGIN),
        env.get(DB_PASSWORD)
      );
    boolean doUpdate = parseDoUpdate(args);
    
    try (DatabaseInstance appDb = new DatabaseInstance(dbConf)) {
      ApolloCommentUpdater commentUpdater = new ApolloCommentUpdater(env.get(SOLR_URL), appDb, env.get(PROJECT_ID));
      if (doUpdate) commentUpdater.syncAll();
      else commentUpdater.findDocumentsToUpdate();
    }
  }  
}
