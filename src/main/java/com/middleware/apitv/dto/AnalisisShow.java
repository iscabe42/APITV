package com.middleware.apitv.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "analisis_show")
public class AnalisisShow {

    @Id
    private String id;

    @Field("show_id")
    private Long showId;

    private String comment;
    private int rating;

    public AnalisisShow() {}

    public AnalisisShow(Long showId, String comment, int rating) {
        this.showId = showId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
