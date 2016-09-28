package com.richrelevance;

import android.content.Context;

import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.recommendations.Creative;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.recommendations.PlacementsPersonalizeBuilder;
import com.richrelevance.recommendations.PlacementsRecommendationsBuilder;
import com.richrelevance.recommendations.Product;
import com.richrelevance.recommendations.ProductBuilder;
import com.richrelevance.recommendations.RecommendedProduct;
import com.richrelevance.recommendations.StrategyRecommendationsBuilder;
import com.richrelevance.recommendations.StrategyType;
import com.richrelevance.userPreference.ActionType;
import com.richrelevance.userPreference.FieldType;
import com.richrelevance.userPreference.UserPreferenceBuilder;
import com.richrelevance.userProfile.UserProfileBuilder;
import com.richrelevance.userProfile.UserProfileField;

import java.util.Collection;

/**
 * The central launch point for all Rich Relevance SDK activity and configuration.
 */
public class RichRelevance {

    private static WebRequestManager webRequestManager = new WebRequestManager(2);

    private static RichRelevanceClient defaultClient = newClient();

    private RichRelevance() {
    }

    static WebRequestManager getWebRequestManager() {
        return webRequestManager;
    }

    /**
     * @return The default client used for requests.
     */
    public static RichRelevanceClient getDefaultClient() {
        return RichRelevance.defaultClient;
    }

    /**
     * Sets the default {@link RichRelevanceClient} to use for requests.
     *
     * @param client The default client to use for requests.
     */
    public static void setDefaultClient(RichRelevanceClient client) {
        RichRelevance.defaultClient = client;
    }

    /**
     * Creates a new {@link RichRelevanceClient}.
     *
     * @return A new client.
     */
    public static RichRelevanceClient newClient() {
        return new RichRelevanceClientImpl();
    }

    /**
     * Sets the level of logs which will be logged.
     *
     * @param logLevel The lowest log level to log.
     */
    public static void setLoggingLevel(@RRLog.LogLevel int logLevel) {
        RRLog.setLogLevel(logLevel);
    }

    /**
     * Initializes the Rich Relevance SDK.
     *
     * @param context A context to use to access system resources.
     * @param config  The configuration to use to access the API.
     */
    public static void init(Context context, ClientConfiguration config) {
        ClickTrackingManager.getInstance().init(context);
        getDefaultClient().setConfiguration(config);
    }

    // region Fetching

    /**
     * Creates a builder which requests product recommendations using a specified strategy.
     *
     * @param strategy The strategy to use to obtain recommendations.
     * @return The created builder.
     */
    public static StrategyRecommendationsBuilder buildRecommendationsUsingStrategy(StrategyType strategy) {
        return new StrategyRecommendationsBuilder()
                .setStrategy(strategy);
    }

    /**
     * Creates a builder which requests product recommendations for the specified category and placements.
     *
     * @param categoryId The category ID.
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForCategory(String categoryId, Placement... placements) {
        return new PlacementsRecommendationsBuilder()
                .setCategoryId(categoryId)
                .addPlacements(placements);
    }

    /**
     * Creates a builder which requests product recommendations for the specified category and placements.
     *
     * @param categoryId The category ID.
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForCategory(String categoryId, Collection<Placement> placements) {
        return new PlacementsRecommendationsBuilder()
                .setCategoryId(categoryId)
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests product recommendations for the specified category and placements.
     *
     * @param searchTerm The search term.
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForSearchTerm(String searchTerm, Placement... placements) {
        return new PlacementsRecommendationsBuilder()
                .setSearchTerm(searchTerm)
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests product recommendations for the specified category and placements.
     *
     * @param searchTerm The search term.
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForSearchTerm(String searchTerm, Collection<Placement> placements) {
        return new PlacementsRecommendationsBuilder()
                .setSearchTerm(searchTerm)
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests product recommendations for the specified placements, personalized to the current user.
     *
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForPlacements(Placement... placements) {
        return new PlacementsRecommendationsBuilder()
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests product recommendations for the specified placements, personalized to the current user.
     *
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsRecommendationsBuilder buildRecommendationsForPlacements(Collection<Placement> placements) {
        return new PlacementsRecommendationsBuilder()
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests creatives content for the specified placements, personalized to the current user.
     *
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsPersonalizeBuilder buildPersonalizations(Placement... placements) {
        return new PlacementsPersonalizeBuilder()
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests creatives content for the specified placements, personalized to the current user.
     *
     * @param placements The desired placements.
     * @return The created builder.
     */
    public static PlacementsPersonalizeBuilder buildPersonalizations(Collection<Placement> placements) {
        return new PlacementsPersonalizeBuilder()
                .setPlacements(placements);
    }

    /**
     * Creates a builder which requests user preferences.
     *
     * @param fields The desired field types.
     * @return The created builder.
     */
    public static UserPreferenceBuilder buildGetUserPreferences(FieldType... fields) {
        return new UserPreferenceBuilder(fields);
    }

    /**
     * Creates a builder which requests user preferences.
     *
     * @param fields The desired field types.
     * @return The created builder.
     */
    public static UserPreferenceBuilder buildGetUserPreferences(Collection<FieldType> fields) {
        return new UserPreferenceBuilder(fields);
    }

    /**
     * Creates a builder which requests products.
     *
     * @param productIds The desired products.
     * @return The created builder.
     */
    public static ProductBuilder buildProductsRequest(String... productIds) {
        return new ProductBuilder().setProducts(productIds);
    }

    /**
     * Creates a builder which requests products.
     *
     * @param productIds The desired products.
     * @return The created builder.
     */
    public static ProductBuilder buildProductsRequest(Collection<String> productIds) {
        return new ProductBuilder().setProducts(productIds);
    }

    /**
     * Creates a builder which requests user profile information.
     *
     * @param fields The desired field types.
     * @return The created builder.
     */
    public static UserProfileBuilder buildGetUserProfile(UserProfileField... fields) {
        return new UserProfileBuilder()
                .setFields(fields);
    }

    /**
     * Creates a builder which requests user profile information.
     *
     * @param fields The desired field types.
     * @return The created builder.
     */
    public static UserProfileBuilder buildGetUserProfile(Collection<UserProfileField> fields) {
        return new UserProfileBuilder()
                .setFields(fields);
    }

    // endregion Fetching

    // region Tracking

    /**
     * Creates a builder which tracks a product view.
     *
     * @param placement The placement.
     * @param productId The ID of the product that was viewed.
     * @return The created builder.
     */
    public static RequestBuilder<?> buildProductView(Placement placement, String productId) {
        return new PlacementsRecommendationsBuilder()
                .addPlacements(placement)
                .setProductIds(productId);
    }

    /**
     * Creates a builder which tracks a purchase.
     *
     * @param placement The placement.
     * @param orderId   The ID of the order for the purchase.
     * @param products  The products that were purchased.
     * @return The created builder.
     */
    public static RequestBuilder<?> buildLogPurchase(Placement placement, String orderId, Product... products) {
        return new PlacementsRecommendationsBuilder()
                .addPlacements(placement)
                .setOrderId(orderId)
                .addPurchasedProducts(products);
    }

    /**
     * Creates a builder which tracks a purchase.
     *
     * @param placement The placement.
     * @param orderId   The ID of the order for the purchase.
     * @param products  The products that were purchased.
     * @return The created builder.
     */
    public static RequestBuilder<?> buildLogPurchase(Placement placement, String orderId, Collection<Product> products) {
        return new PlacementsRecommendationsBuilder()
                .addPlacements(placement)
                .setOrderId(orderId)
                .addPurchasedProducts(products);
    }

    /**
     * Creates a builder which tracks a user preference.
     *
     * @param target The target field type.
     * @param action The action type the user performed.
     * @param ids    The IDs of the items the user performed the action on.
     * @return The created builder.
     */
    public static UserPreferenceBuilder buildTrackUserPreference(FieldType target, ActionType action, String... ids) {
        return new UserPreferenceBuilder(target, action, ids);
    }

    /**
     * Creates a builder which tracks the user liking the specified products.
     *
     * @param productIds The IDs of the products the user liked.
     * @return The created builder.
     */
    public static RequestBuilder<?> buildProductLike(String... productIds) {
        return buildTrackUserPreference(FieldType.PRODUCT, ActionType.LIKE, productIds);
    }

    /**
     * Tracks a user click on a particular recommended product. If no network is available, a best effort is made to
     * queue the request and submit it when the network becomes available.
     *
     * @param product The product the user clicked.
     * @see #flushClickTracking()
     */
    public static void trackClick(RecommendedProduct product) {
        ClickTrackingManager.getInstance().trackClick(product.getClickUrl());
    }

    /**
     * Tracks a user click on a particular creative. If no network is available, a best effort is made to
     * queue the request and submit it when the network becomes available.
     *
     * @param creative The product the user clicked.
     * @see #flushClickTracking()
     */
    public static void trackClick(Creative creative) {
        ClickTrackingManager.getInstance().trackClick(creative.getTrackingUrl());
    }

    /**
     * Attempts to flush the click tracking queue. This will be done automatically when the network becomes available
     * if the {@link android.Manifest.permission#ACCESS_NETWORK_STATE} permission is held. Otherwise, call this method
     * when you would like to reattempt to send the queued click tracking requests.
     */
    public static void flushClickTracking() {
        ClickTrackingManager.getInstance().flush();
    }

    // endregion Tracking


}