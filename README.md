# Site Search User Comment Updater #

Shared Library for handling SOLR updates of user comment information

## Testing
`bin/` contains two scripts for testing.  To run one on dev server:
1. set your gus environment to an up-to-date dev site, using `setenv`. Now your classpath includes `$GUS_HOME/lib/java` which contains some .jars this library needs.
2. `mvn install` will create a new SNAPSHOT .jar. Look in `pom.xml` to see its version
3. copy the new jar from your local `.m2/` repository to `$GUS_HOME/lib/java`. Be sure this is the library's only jar in $GUS_HOME. (The name of the .jar doesn't matter)
4. set environment variables. Either make a local `.env` file from `env.sample`, or use `container_env` from the dev site if its config conforms to your needs
5. run the script like this: `export $(xargs < my_env_file); bin/apiUserCommentUpdater`
6. if you want to really update solr, add the `--doUpdate` flag
