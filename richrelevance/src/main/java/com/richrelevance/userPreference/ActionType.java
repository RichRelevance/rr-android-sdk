package com.richrelevance.userPreference;

public enum ActionType {

    DISLIKE {
        @Override
        public String getKey() {
            return "dislike";
        }
    },
    LIKE {
        @Override
        public String getKey() {
            return "like";
        }
    },
    /**
     * Undoes a previous like or dislike.
     */
    NEUTRAL {
        @Override
        public String getKey() {
            return "neutral";
        }
    },
    /**
     * Flags products that should not be recommended without disliking them (for example, if a customer already owns a
     * particular product, they might like it but not want to see it again). Products flagged with notForRecs will also
     * be removed as seeds from all
     * <a href="https://help.richrelevance.com/3_Recommend/Strategies/Recommendation_Strategies_Guide#section_6">Personalized Strategies</a>.
     */
    NOT_FOR_RECS {
        @Override
        public String getKey() {
            return "notForRecs";
        }
    };

    public abstract String getKey();
}
