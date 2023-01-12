package net.defekt.mc.chatclient.ui;

public class SearchQuery {
    public enum SearchType {
        NAME, AUTHOR, DESCRIPTION, WEBSITE;
    }

    private final SearchType type;
    private final String query;

    public SearchQuery(String query, SearchType type) {
        this.type = type;
        this.query = query;
    }

    public SearchType getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }
}
