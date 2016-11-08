package com.richrelevance.richrelevance.FindDemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.richrelevance.find.autocomplete.AutoCompleteSuggestion;

public class AutoCompleteSearchSuggestion extends AutoCompleteSuggestion implements SearchSuggestion {

    public AutoCompleteSearchSuggestion(AutoCompleteSuggestion rrAutoCompleteSuggestion) {
        super(rrAutoCompleteSuggestion.getId(), rrAutoCompleteSuggestion.getTerms(), rrAutoCompleteSuggestion.getType(), rrAutoCompleteSuggestion.getValue());
    }

    public AutoCompleteSearchSuggestion(String id, String terms, String type, int value) {
        super(id, terms, type, value);
    }

    @Override
    public String getBody() {
        return getTerms();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getTerms());
        dest.writeString(getType());
        dest.writeInt(getValue());
    }

    public static final Parcelable.Creator<AutoCompleteSearchSuggestion> CREATOR = new Parcelable.Creator<AutoCompleteSearchSuggestion>() {
        @Override
        public AutoCompleteSearchSuggestion createFromParcel(Parcel in) {
            return new AutoCompleteSearchSuggestion(in.readString(), in.readString(), in.readString(), in.readInt());
        }

        @Override
        public AutoCompleteSearchSuggestion[] newArray(int size) {
            return new AutoCompleteSearchSuggestion[size];
        }
    };

}
