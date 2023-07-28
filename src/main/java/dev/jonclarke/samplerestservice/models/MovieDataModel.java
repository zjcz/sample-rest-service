package dev.jonclarke.samplerestservice.models;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Movie entity object.
 */

@JacksonXmlRootElement(localName="Movie")
@Entity
public class MovieDataModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private LocalDateTime releaseDate;
    private Boolean availableOnDvd;

    public MovieDataModel() {

    }

    public MovieDataModel(final String title, final String description, final LocalDateTime releaseDate, final boolean availableOnDvd) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.availableOnDvd = availableOnDvd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isAvailableOnDvd() {
        return availableOnDvd;
    }

    public void setAvailableOnDvd(Boolean availableOnDvd) {
        this.availableOnDvd = availableOnDvd;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof MovieDataModel)) {
            return false;
        }

        MovieDataModel item = (MovieDataModel) o;
        return Objects.equals(this.id, item.id) && Objects.equals(this.title, item.title)
                && Objects.equals(this.description, item.description)
                && Objects.equals(this.releaseDate, item.releaseDate)
                && Objects.equals(this.availableOnDvd, item.availableOnDvd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description, this.releaseDate, this.availableOnDvd);
    }
    @Override
    public String toString() {
        return "MovieDataModel{" + "id=" + this.id + ", title='" + this.title + '\''
                + ", description='" + this.description + '\''
                + ", releaseDate='" + this.releaseDate + '\''
                + ", availableOnDvd='" + this.availableOnDvd + '\'' + '}';
    }


}

