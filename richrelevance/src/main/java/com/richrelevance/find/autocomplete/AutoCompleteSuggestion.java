package com.richrelevance.find.autocomplete;

/**
 *  An auto-complete suggestion from the Auto-complete response
 */
public class AutoCompleteSuggestion {

    /**
     * Identifier for the auto complete suggestion
     */
    private String id;

    /**
     * The auto complete word suggestion
     */
    private String terms;

    /**
     * Type of the suggestion
     */
    private String type;

    /**
     * Indicates popularity score of the suggestion
     */
    private int value;

    public AutoCompleteSuggestion(String id, String terms, String type, int value) {
        this.id = id;
        this.terms = terms;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getTerms() {
        return terms;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
