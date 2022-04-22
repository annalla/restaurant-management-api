package com.restaurant.restaurantmanagementapi.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

/**
 * The MenuItem class wraps information of menu item including id,name, description, image, note and price
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name",unique = true)
    @NotNull
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String image;
    @Column(name = "note")
    private String note;
    @NotNull
    @Column(name = "price")
    private Double price;
    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    public MenuItem(String name, String description, String image, String note, Double price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.note = note;
        this.price = price;
        this.status = true;
    }

}
