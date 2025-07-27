package org.example.remotly_ecommerce.domain;

/**
 * Defines different types of category sections that can appear on the home page.
 * These are used to organize and display content dynamically in various layouts.
 *
 * For example, sections may be displayed as grids, featured deals, or grouped by categories.
 *
 * @author Mohamed Sayed
 * @since 27 July 2025
 */
public enum HomeCategorySection {

    /** Categories related to electrical products, like electronics and appliances. */
    ELECTRIC_CATEGORIES,

    /** Displayed in a grid layout, typically with images and quick links. */
    GRID,

    /** General categorization for browsing by product categories. */
    SHOP_BY_CATEGORIES,

    /** Highlighted promotional offers or discounted products. */
    DEALS
}
