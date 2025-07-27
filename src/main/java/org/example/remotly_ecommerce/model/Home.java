package org.example.remotly_ecommerce.model;


import lombok.Data;
import java.util.List;

/**
 * Represents the structure of the homepage layout for an e-commerce platform.
 * It groups various categories and deals to be displayed on the home page UI.
 *
 * This class is not a database entity but a DTO-like object used for returning
 * combined homepage data (e.g. to the frontend).
 *
 * Sections include:
 * - Grid layout categories
 * - Shop by categories
 * - Electric categories
 * - Deal categories
 * - Specific deals
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class Home {

    /**
     * Categories displayed in a grid layout on the homepage.
     */
    private List<HomeCategory> grid;

    /**
     * Categories used for "Shop by Categories" section.
     */
    private List<HomeCategory> shopByCategories;

    /**
     * Categories related to electric or electronics section.
     */
    private List<HomeCategory> electricCategories;

    /**
     * Categories specifically marked for deals section.
     */
    private List<HomeCategory> dealCategories;

    /**
     * List of specific deals to be shown on the homepage.
     */
    private List<Deal> deals;
}
