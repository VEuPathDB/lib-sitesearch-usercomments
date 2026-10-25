package org.veupathdb.sitesearch.usercomments;

import org.gusdb.fgputil.db.platform.SupportedPlatform;
import org.gusdb.fgputil.db.pool.DatabaseInstance;
import org.gusdb.fgputil.db.pool.SimpleDbConfig;
import org.veupathdb.lib.ldap.LDAP;
import org.veupathdb.lib.ldap.LDAPConfig;
import org.veupathdb.lib.ldap.LDAPHost;
import org.veupathdb.lib.ldap.NetDesc;

import java.util.List;

public class ApolloCommentUpdaterCli extends CommentUpdaterCli {
  
  private final static String DB_LDAP_SERVER = "APPDB_LDAP_SERVER";
  private final static String DB_LDAP_BASE_DN = "APPDB_LDAP_BASE_DN";
  private final static String DB_NAME = "APPDB_LDAP_CN";
  private final static String DB_LOGIN = "APPDB_LOGIN";
  private final static String DB_PASSWORD = "APPDB_PASSWORD";
  private final static String SOLR_URL = "SOLR_URL";
 
  public static void main(String[] args) throws Exception {

    String[] envVarKeys = {DB_LDAP_SERVER, DB_LDAP_BASE_DN, DB_NAME, DB_LOGIN, DB_PASSWORD, SOLR_URL};

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
    
    try (DatabaseInstance appDb = new DatabaseInstance(dbConf)) {
      ApolloCommentUpdater commentUpdater = new ApolloCommentUpdater(env.get(SOLR_URL), appDb);
      if (doUpdate) commentUpdater.syncAll();
      else commentUpdater.findDocumentsToUpdate();
    }
  }  
}
