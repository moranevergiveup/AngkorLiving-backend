package com.mora.angkorleving_backend.DTOs.response;

import com.mora.angkorleving_backend.model.Floor;
import lombok.AllArgsConstructor;
import lombok.Data;

//@Data
//@AllArgsConstructor
//public class FloorResponse {
//    private Long id;
//    private String name;
//    private String description;
//    private Double price;
//    private Integer getnumberOfRooms;
//}
@Data
@AllArgsConstructor
public class FloorResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer numberOfRooms;

    public FloorResponse(Floor floor) {
        this.id = floor.getId();
        this.name = floor.getName();
        this.description = floor.getDescription();
        this.price = floor.getPrice();
        this.numberOfRooms = floor.getNumberOfRooms();
    }
}
