package com.richrelevance.recommendations;

import com.richrelevance.RequestBuilder;
import com.richrelevance.ResponseInfo;
import com.richrelevance.utils.Utils;
import com.richrelevance.utils.ValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class PlacementsBuilder<PlacementsResponseInfo extends ResponseInfo, Builder extends PlacementsBuilder> extends RequestBuilder<PlacementsResponseInfo> {

    public static class Keys {
        public static final String PLACEMENTS = "placements";
        public static final String PAGE_FEATURED_BRAND = "fpb";
        public static final String EXCLUDE_HTML = "excludeHtml";
        public static final String INCLUDE_CATEGORY_DATA = "categoryData";
        public static final String USER_ATTRIBUTES = "userAttribute";
        public static final String REFERRER = "pref";
        public static final String CATEGORY_HINT_IDS = "chi";
        public static final String USER_SEGMENTS = "sgs";
        public static final String REGION_ID = "rid";
        public static final String VIEWED_PRODUCTS = "viewed";
        public static final String PURCHASED_PRODUCTS = "purchased";
    }

    /**
     * Sets the list of placements. Each identifier consists of a page type (see valid page types below) and a placement
     * name. <ul> <li>You will get one set of recommendations for each placement.</li> <li>All placements must be for
     * the same page type. </li> <li>The first placement is assumed to be the "best" placement and will receive the best
     * recommendation strategy. </li> <li>When multiple placements are requested in the same call, each will receive a
     * unique strategy and unique products.</li> </ul>
     *
     * @param placements The placements to use.
     * @return This builder for chaining method calls.
     */
    public Builder setPlacements(Placement... placements) {
        setListParameter(Keys.PLACEMENTS, getPlacementStrings(Utils.safeAsList(placements)));
        return (Builder) this;
    }

    /**
     * Sets the list of placements. Each identifier consists of a page type (see valid page types below) and a placement
     * name. <ul> <li>You will get one set of recommendations for each placement.</li> <li>All placements must be for
     * the same page type. </li> <li>The first placement is assumed to be the "best" placement and will receive the best
     * recommendation strategy. </li> <li>When multiple placements are requested in the same call, each will receive a
     * unique strategy and unique products.</li> </ul>
     *
     * @param placements The placements to use.
     * @return This builder for chaining method calls.
     */
    public Builder setPlacements(Collection<Placement> placements) {
        setListParameter(Keys.PLACEMENTS, getPlacementStrings(placements));
        return (Builder) this;
    }

    /**
     * The brand featured on the page. Used to set the seed for brand-seeded strategies like Brand Top Sellers.
     *
     * @param brand The brand featured on the page.
     * @return This builder for chaining method calls.
     */
    public PlacementsBuilder setPageFeaturedBrand(String brand) {
        setParameter(Keys.PAGE_FEATURED_BRAND, brand);
        return this;
    }

    /**
     * If set to false, omits category data in the response. If true, categoryIds and categories are returned in the
     * response. Default state: true.
     *
     * @param returnCategoryData True to include category data.
     * @return This builder for chaining method calls.
     */
    public Builder setReturnCategoryData(boolean returnCategoryData) {
        setParameter(Keys.INCLUDE_CATEGORY_DATA, returnCategoryData);
        return (Builder) this;
    }

    /**
     * Sets the key/value pairs describing the attribute context of the current of the user.
     *
     * @param attributes The map of attributes to set.
     * @return This builder for chaining method calls.
     */
    public Builder setUserAttributes(ValueMap<String> attributes) {
        setValueMapParameter(Keys.USER_ATTRIBUTES, attributes);
        return (Builder) this;
    }

    /**
     * Sets category hint IDs. Category hints can be added to any page type. Several category hints can be added on a
     * single page. Each category hint added qualifies the page for merchandising rules that are associated to the
     * category.
     *
     * @param hintIds The category hint IDs to set.
     * @return This builder for chaining method calls.
     */
    public Builder setCategoryHintIds(String... hintIds) {
        setListParameter(Keys.CATEGORY_HINT_IDS, hintIds);
        return (Builder) this;
    }

    /**
     * Sets category hint IDs. Category hints can be added to any page type. Several category hints can be added on a
     * single page. Each category hint added qualifies the page for merchandising rules that are associated to the
     * category.
     *
     * @param hintIds The category hint IDs to set.
     * @return This builder for chaining method calls.
     */
    public Builder setCategoryHintIds(Collection<String> hintIds) {
        setListParameter(Keys.CATEGORY_HINT_IDS, hintIds);
        return (Builder) this;
    }

    /**
     * To supply user segments. Should be passed in to have a segment targeted campaign work correctly.
     *
     * @param segments The user segments to set.
     *
     * @return This builder for chaining method calls.
     */
    public Builder setUserSegments(ValueMap<String> segments) {
        setValueMapParameter(Keys.USER_SEGMENTS, segments);
        return (Builder) this;
    }

    /**
     * Sets the region ID. Must be consistent with the ID used in the <a href="http://developer.richrelevance.com/?page_id=403">product
     * region feed</a>.
     *
     * @param regionId The region ID to set.
     * @return This builder for chaining method calls.
     */
    public Builder setRegionId(String regionId) {
        setParameter(Keys.REGION_ID, regionId);
        return (Builder) this;
    }

    /**
     * List the product IDs of the product detail pages user viewed in the current session, including timestamps in
     * milliseconds using UTC time zone for each view.
     *
     * @param products A map of the product IDs mapped to the view timestamps.
     * @return This builder for chaining method calls.
     */
    public Builder setViewedProducts(ValueMap<Long> products) {
        setValueMapParameter(Keys.VIEWED_PRODUCTS, products);
        return (Builder) this;
    }

    /**
     * List the product IDs of the products the user purchased in the current session, including timestamps in
     * milliseconds using UTC time zone for each purchase.
     *
     * @param products A map of the product IDs mapped to the purchase time stamps.
     * @return This builder for chaining method calls.
     */
    public Builder setPurchasedProducts(ValueMap<Long> products) {
        setValueMapParameter(Keys.PURCHASED_PRODUCTS, products);
        return (Builder) this;
    }

    /**
     * Shopper's referrer prior to viewing this page. Used for reporting and merchandising. Highly recommended.
     *
     * @param referrer The shopper's referrer prior to viewing this page.
     * @return This builder for chaining method calls.
     */
    public Builder setReferrer(String referrer) {
        setParameter(Keys.REFERRER, referrer);
        return (Builder) this;
    }

    /**
     * Adds to the list of placements. Each identifier consists of a page type (see valid page types below) and a
     * placement name. <ul> <li>You will get one set of recommendations for each placement.</li> <li>All placements must
     * be for the same page type. </li> <li>The first placement is assumed to be the "best" placement and will receive
     * the best recommendation strategy. </li> <li>When multiple placements are requested in the same call, each will
     * receive a unique strategy and unique products.</li> </ul>
     *
     * @param placements The placements to add.
     * @return This builder for chaining method calls.
     */
    public Builder addPlacements(Placement... placements) {
        addListParameters(Keys.PLACEMENTS, getPlacementStrings(Utils.safeAsList(placements)));
        return (Builder) this;
    }

    /**
     * If set to true, omits the HTML returned in the Relevance Cloud server response. If false, the response includes
     * the HTML for the placement, which is set in the layout, in the html field. Default = true.
     *
     * @param exclude True to omit the returned HTML.
     * @return This builder for chaining method calls.
     */
    public Builder excludeHtml(boolean exclude) {
        setParameter(Keys.EXCLUDE_HTML, exclude);
        return (Builder) this;
    }

    private Collection<String> getPlacementStrings(Collection<Placement> placements) {
        if(placements != null) {
            List<String> stringPlacements = new ArrayList<>(placements.size());

            for(Placement placement : placements) {
                if(placement != null) {
                    stringPlacements.add(placement.getApiValue());
                }
            }

            return stringPlacements;
        }

        return null;
    }
}
