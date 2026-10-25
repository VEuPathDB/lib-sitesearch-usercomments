package org.veupathdb.sitesearch.usercomments;

import org.gusdb.fgputil.db.platform.SupportedPlatform;
import org.gusdb.fgputil.db.pool.DatabaseInstance;
import org.gusdb.fgputil.db.pool.SimpleDbConfig;
import org.veupathdb.lib.ldap.*;

import java.util.List;

public class UserCommentUpdaterCli extends CommentUpdaterCli {
  
  private final static String DB_LDAP_SERVER = "USERDB_LDAP_SERVER";
  private final static String DB_LDAP_BASE_DN = "USERDB_LDAP_BASE_DN";
  private final static String DB_NAME = "USERDB_LDAP_CN";
  private final static String DB_LOGIN = "USERDB_LOGIN";
  private final static String DB_PASSWORD = "USERDB_PASSWORD";
  private final static String COMMENT_SCHEMA = "USERDB_COMMENT_SCHEMA";
  private final static String SOLR_URL = "SOLR_URL";
 
  public static void main(String[] args) throws Exception {

    String[] envVarKeys = {DB_LDAP_SERVER, DB_LDAP_BASE_DN, DB_NAME, DB_LOGIN, DB_PASSWORD, SOLR_URL, COMMENT_SCHEMA};
    validateEnv(envVarKeys);

    final var env = System.getenv();
    LDAP ldap = new LDAP(new LDAPConfig(List.of(LDAPHost.ofString(env.get(DB_LDAP_SERVER))), env.get(DB_LDAP_BASE_DN)));
    NetDesc netDesc = ldap.requireSingularNetDesc(env.get(DB_NAME));

    SimpleDbConfig dbConf = SimpleDbConfig.create(
        SupportedPlatform.POSTGRESQL,
        SupportedPlatform.POSTGRESQL.getPlatformInstance().getConnectionUrl(netDesc),
        env.get(DB_LOGIN),
        env.get(DB_PASSWORD)
      );
    boolean doUpdate = parseDoUpdate(args);
    
    try (DatabaseInstance userDb = new DatabaseInstance(dbConf)) {
      UserCommentUpdater commentUpdater = new UserCommentUpdater(env.get(SOLR_URL), userDb, env.get(COMMENT_SCHEMA));
      if (doUpdate) commentUpdater.syncAll();
      else commentUpdater.findDocumentsToUpdate();
    }
  }  
}
