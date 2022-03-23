package com.restaurant.restaurantmanagementapi.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name ="menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name",unique = true)
    @NotNull
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="image")
    private String image;
    @Column(name="note")
    private String note;
    @NotNull
    @Column(name="price")
    private Double price;
    @Column(name="is_deleted",columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", note='" + note + '\'' +
                ", price=" + price +
                ", isDeleted=" + isDeleted +
                '}';
    }
    //    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="menu_type_2_id", nullable=false)
//    private MenuTypeLevel2 menuTypeLevel2;
//    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<MenuTypeLevel2> menuTypes;

}
