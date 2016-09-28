package com.richrelevance.userProfile;

import com.richrelevance.ResponseInfo;

import org.json.JSONObject;

import java.util.List;

public class UserProfileResponseInfo extends ResponseInfo {

    private String userId;
    private String mostRecentRRUserGuid;
    private long timeOfFirstEvent;

    private List<UserProfileElement.ReferrerUrl> referrerUrls;
    private List<UserProfileElement.UserSegment> userSegments;
    private List<UserProfileElement.UserAttribute> userAttributes;
    private List<UserProfileElement.VerbNoun> verbNouns;

    private List<UserProfileElement.ViewedItem> viewedItems;
    private List<UserProfileElement.ViewedCategory> viewedCategories;
    private List<UserProfileElement.ViewedBrand> viewedBrands;

    private List<UserProfileElement.ClickedItem> clickedItems;
    private List<UserProfileElement.SearchedTerm> searchedTerms;
    private List<UserProfileElement.AddedToCartItem> addedToCartItems;
    private List<UserProfileElement.Order> orders;
    private List<UserProfileElement.CountedEvent> countedEvents;

    private JSONObject batchAttributes;

    /**
     * @return The merchant's user ID for the profile.
     */
    public String getUserId() {
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The most recent rrUserGuid seen with this profile. The intent is that this can be used for cookie
     * synchronization across devices.
     */
    public String getMostRecentRRUserGuid() {
        return mostRecentRRUserGuid;
    }

    void setMostRecentRRUserGuid(String mostRecentRRUserGuid) {
        this.mostRecentRRUserGuid = mostRecentRRUserGuid;
    }

    /**
     * @return The time of the first event recorded for this profile. This is not necessarily recoverable from the list
     * of events in the profile because some of the older ones may have been discarded.
     */
    public long getTimeOfFirstEvent() {
        return timeOfFirstEvent;
    }

    void setTimeOfFirstEvent(long timeOfFirstEvent) {
        this.timeOfFirstEvent = timeOfFirstEvent;
    }

    public List<UserProfileElement.ReferrerUrl> getReferrerUrls() {
        return referrerUrls;
    }

    void setReferrerUrls(List<UserProfileElement.ReferrerUrl> referrerUrls) {
        this.referrerUrls = referrerUrls;
    }

    /**
     * @return A list of the most recent user segments seen in the instrumentation for this user. For this field we
     * just return the seen segments with the most recent segments first.
     */
    public List<UserProfileElement.UserSegment> getUserSegments() {
        return userSegments;
    }

    void setUserSegments(List<UserProfileElement.UserSegment> userSegments) {
        this.userSegments = userSegments;
    }

    /**
     * @return A list of the most recent user attribute value pairs seen in the instrumentation.
     */
    public List<UserProfileElement.UserAttribute> getUserAttributes() {
        return userAttributes;
    }

    void setUserAttributes(List<UserProfileElement.UserAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }

    /**
     * @return A list of the most recent verb/noun events seen in the instrumentation.
     */
    public List<UserProfileElement.VerbNoun> getVerbNouns() {
        return verbNouns;
    }

    void setVerbNouns(List<UserProfileElement.VerbNoun> verbNouns) {
        this.verbNouns = verbNouns;
    }

    /**
     * @return A list of the most recent itemView events of the user. These typically correspond to the user viewing an
     * item page on the merchants website. For each event we return the sessionId, itemId (external ID), and timestamp
     * of the event.
     */
    public List<UserProfileElement.ViewedItem> getViewedItems() {
        return viewedItems;
    }

    void setViewedItems(List<UserProfileElement.ViewedItem> viewedItems) {
        this.viewedItems = viewedItems;
    }

    /**
     * @return A list of the most recent categoryView events of the user. These typically correspond to views of the
     * merchants category page. The sessionId and timestamp have the same meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.ViewedCategory> getViewedCategories() {
        return viewedCategories;
    }

    void setViewedCategories(List<UserProfileElement.ViewedCategory> viewedCategories) {
        this.viewedCategories = viewedCategories;
    }

    /**
     * @return A list of the most recent brandView events of the user. These typically correspond to views of the
     * merchant's brand page. The sessionId and timestamp have the same meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.ViewedBrand> getViewedBrands() {
        return viewedBrands;
    }

    void setViewedBrands(List<UserProfileElement.ViewedBrand> viewedBrands) {
        this.viewedBrands = viewedBrands;
    }

    /**
     * @return A list of the IDs of items the users most recently clicked on recommendations for. The sessionId and
     * timestamp have the same meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.ClickedItem> getClickedItems() {
        return clickedItems;
    }

    void setClickedItems(List<UserProfileElement.ClickedItem> clickedItems) {
        this.clickedItems = clickedItems;
    }

    /**
     * @return A list of the string the users most recently searched for. The sessionId and timestamp have the same
     * meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.SearchedTerm> getSearchedTerms() {
        return searchedTerms;
    }

    void setSearchedTerms(List<UserProfileElement.SearchedTerm> searchedTerms) {
        this.searchedTerms = searchedTerms;
    }

    /**
     * @return A list of the most recent cartAdd events of the user. For each event we record the itemId, quantity and
     * priceInCents. The sessionId and timestamp have the same meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.AddedToCartItem> getAddedToCartItems() {
        return addedToCartItems;
    }

    void setAddedToCartItems(List<UserProfileElement.AddedToCartItem> addedToCartItems) {
        this.addedToCartItems = addedToCartItems;
    }

    /**
     * @return A list of the most recent purchase events of the user. For each event we record the itemId, quantity and
     * priceInCents. The sessionId and timestamp have the same meaning as for {@link #getViewedItems()}.
     */
    public List<UserProfileElement.Order> getOrders() {
        return orders;
    }

    void setOrders(List<UserProfileElement.Order> orders) {
        this.orders = orders;
    }

    /**
     * @return A list of the most recent counted events seen in the instrumentation, the number of times it has been
     * seen and the time of the most recent event.
     */
    public List<UserProfileElement.CountedEvent> getCountedEvents() {
        return countedEvents;
    }

    void setCountedEvents(List<UserProfileElement.CountedEvent> countedEvents) {
        this.countedEvents = countedEvents;
    }

    /**
     * @return A {@link JSONObject} containing extra attributes.
     */
    public JSONObject getBatchAttributes() {
        return batchAttributes;
    }

    void setBatchAttributes(JSONObject batchAttributes) {
        this.batchAttributes = batchAttributes;
    }
}
