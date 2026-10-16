package org.veupathdb.sitesearch.usercomments.solr;

public enum Op {
  AND,
  OR;

  @Override
  public String toString() {
    return " " + name() + " ";
  }
}
