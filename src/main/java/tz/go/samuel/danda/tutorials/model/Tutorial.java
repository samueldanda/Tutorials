package tz.go.samuel.danda.tutorials.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tutorials")
@Schema(description = "Tutorial Model Information")
@SQLDelete(sql = "UPDATE tutorials SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Tutorial Id", example = "123")
    private long id;

    @Column(name = "title")
    @Schema(description = "Tutorial's title", example = "Swagger Tutorial")
    private String title;

    @Column(name = "description")
    @Schema(description = "Tutorial's description", example = "How to make REST API with PHP")
    private String description;

    @Column(name = "published")
    @Schema(description = "Tutorial's status (published or not)", example = "true")
    private boolean published;


    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

    public Tutorial() {
    }

    public Tutorial(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
        this.published = published;
    }

    public long getId() {
        return id;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Tutorial{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                '}';
    }
}
