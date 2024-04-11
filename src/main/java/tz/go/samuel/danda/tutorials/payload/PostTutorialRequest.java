package tz.go.samuel.danda.tutorials.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostTutorialRequest {

    @NotBlank(message = "The tutorial title is required.")
    @Size(min = 1, max = 60, message = "The tutorial title must be less than 61 characters")
    private String title;

    @NotBlank(message = "The tutorial description is required.")
    @Size(min = 1, max = 60, message = "The tutorial title must be less than 61 characters")
    private String description;

    private Boolean published;

    public PostTutorialRequest() {
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

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
