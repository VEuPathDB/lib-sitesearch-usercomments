package org.veupathdb.sitesearch.usercomments;

import org.gusdb.fgputil.db.platform.SupportedPlatform;
import org.gusdb.fgputil.db.pool.DatabaseInstance;
import org.gusdb.fgputil.db.pool.SimpleDbConfig;

public class UserCommentUpdaterCli extends CommentUpdaterCli {
  
  private final static String DB_HOST = "USERDB_HOST";
  private final static String DB_PORT = "USERDB_PORT";
  private final static String DB_NAME = "USERDB_LDAP_CN";
  private final static String DB_LOGIN = "USERDB_LOGIN";
  private final static String DB_PASSWORD = "USERDB_PASSWORD";
  private final static String COMMENT_SCHEMA = "USERDB_COMMENT_SCHEMA";
  private final static String SOLR_URL = "SOLR_URL";
 
  public static void main(String[] args) throws Exception {

    String[] envVarKeys = {DB_HOST, DB_PORT, DB_NAME, DB_LOGIN, DB_PASSWORD, SOLR_URL, COMMENT_SCHEMA};
    validateEnv(envVarKeys);

    final var env = System.getenv();    
    SimpleDbConfig dbConf = SimpleDbConfig.create(
        SupportedPlatform.POSTGRESQL,
        SupportedPlatform.POSTGRESQL.getPlatformInstance().getConnectionUrl(env.get(DB_HOST), Integer.parseInt(env.get(DB_PORT)), env.get(DB_NAME)),
        env.get(DB_LOGIN),
        env.get(DB_PASSWORD)
      );
    
    try (DatabaseInstance userDb = new DatabaseInstance(dbConf)) {
      UserCommentUpdater commentUpdater = new UserCommentUpdater(env.get(SOLR_URL), userDb, env.get(COMMENT_SCHEMA));
      commentUpdater.syncAll();
    }
  }  
}
