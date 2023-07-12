package net.defekt.mc.chatclient.ui;

public class SearchQuery {
    private final SearchType type;
    private final String query;
    public SearchQuery(final String query, final SearchType type) {
        this.type = type;
        this.query = query;
    }

    public SearchType getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }

    public enum SearchType {
        NAME,
        AUTHOR,
        DESCRIPTION,
        WEBSITE;
    }
}
