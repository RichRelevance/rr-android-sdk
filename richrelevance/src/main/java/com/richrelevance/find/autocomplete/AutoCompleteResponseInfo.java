package com.richrelevance.find.autocomplete;


import com.richrelevance.ResponseInfo;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteResponseInfo extends ResponseInfo {

    private List<AutoCompleteSuggestion> suggestions = new ArrayList<>();

    private List<String> suggestionStrings = new ArrayList<>();

    public void setSuggestions(List<AutoCompleteSuggestion> suggestions) {
        this.suggestions = suggestions;

        for(AutoCompleteSuggestion suggestion : this.suggestions) {
            suggestionStrings.add(suggestion.getTerms());
        }
    }

    public List<AutoCompleteSuggestion> getSuggestions() {
        return suggestions;
    }

    public List<String> getSuggestionStrings() {
        return suggestionStrings;
    }
}
