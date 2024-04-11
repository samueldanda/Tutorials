package tz.go.samuel.danda.tutorials.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tz.go.samuel.danda.tutorials.model.Tutorial;
import tz.go.samuel.danda.tutorials.payload.PostTutorialRequest;
import tz.go.samuel.danda.tutorials.repository.TutorialRepository;

import java.util.*;

@Validated
@RestController
@RequestMapping("${samuel.danda.base-url}")
@CrossOrigin(origins = "http://localhost:8081")
@Tag(name = "Tutorials", description = "Tutorial management APIs")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @Operation(
            description = "Get a a list of Tutorial objects by optionally " +
                    "specifying its title keyword. The response is list Tutorial " +
                    "object with id, title, description and published status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success.",
                    content = {@Content(
                            mediaType = "application/json"
                    )
                    }),
            @ApiResponse(responseCode = "400",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponseBody")
                    )
                    })
    })
    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
            @Parameter(description = "Search Tutorials by title")
            @RequestParam(required = false)
            String title,

            @Parameter(description = "Page number, starting from 0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Number of items per page. Min: 1 Max: 100")
            @RequestParam(defaultValue = "20")
            @Positive(message = "page size should be a positive number")
            @Min(value = 1, message = "minimum page size allowed is 1")
            @Max(value = 100, message = "maximum page size allowed is 100")
            int size,

            @RequestParam(defaultValue = "id,asc")
            String[] sort
    ) {
        try {
            List<Sort.Order> orders = new ArrayList<>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Tutorial> tutorials;
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<Tutorial> pageTuts;

            if (title == null) {
                pageTuts = tutorialRepository.findAll(pagingSort);
            } else {
                pageTuts = tutorialRepository.findByTitleContaining(title, pagingSort);
            }

            tutorials = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalPages", pageTuts.getTotalPages());
            response.put("size", size);
            response.put("empty", pageTuts.isEmpty());
            response.put("first", pageTuts.isFirst());
            response.put("last", pageTuts.isLast());
            response.put("sort", pageTuts.getSort());
            response.put("tutorials", tutorials);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            description = "Get a Tutorial object by specifying its id. The response is Tutorial" +
                    " object with id, title, description and published status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Tutorial.class)
                    )
            }),
            @ApiResponse(responseCode = "400",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponseBody")
                    )
            }),
            @ApiResponse(responseCode = "404",
                    description = "The Tutorial with given Id was not found.",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(
            @Valid
            @Positive(message = "The tutorial id must be a positive number")
            @PathVariable("id")
            long id
    ) {
        try {
            Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

//            if (tutorialData.isPresent()) return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
//            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //  The below one line code can replace the above if statement
              return tutorialData.map(tutorial -> new ResponseEntity<>(tutorial, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(
            description = "Create a Tutorial object by specifying its title, description and " +
                    "optionally its published status. The response is Tutorial object with id, " +
                    "title, description and published status."
    )
    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(
            @Valid
            @RequestBody
            PostTutorialRequest postTutorialRequest
    ) {
        Tutorial tutorialRecord = new Tutorial();
        tutorialRecord.setTitle(postTutorialRequest.getTitle());
        tutorialRecord.setDescription(postTutorialRequest.getDescription());
        if (postTutorialRequest.isPublished() == null) {
            tutorialRecord.setPublished(false);
        } else tutorialRecord.setPublished(postTutorialRequest.isPublished());
//        if (postTutorialRequest.isPublished() == null) {
//            tutorialRecord.setPublished(false);
//        }

        try {
            Tutorial _tutorial = tutorialRepository.save(tutorialRecord);
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            description = "Update a Tutorial object by specifying its id in the path and " +
                    "the updates to make. The response is Tutorial object with id, title, " +
                    "description and published status."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Tutorial.class)
                    )
                    }),
            @ApiResponse(responseCode = "400",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ErrorResponseBody")
                    )
                    }),
            @ApiResponse(responseCode = "500",
                    content = {@Content(schema = @Schema())})
    })
    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(
            @PathVariable("id") long id,
            @RequestBody Tutorial tutorial
    ) {
        try {
            Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

            if (tutorialData.isPresent()) {
                Tutorial _tutorial = tutorialData.get();
                _tutorial.setTitle(tutorial.getTitle());
                _tutorial.setDescription(tutorial.getDescription());
                _tutorial.setPublished(tutorial.isPublished());
                return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            description = "Delete a Tutorial object by specifying its id."
    )
    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            description = "Delete all Tutorial objects in the database."
    )
    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(
            description = "Get a list of all Tutorial objects that their published status is true. " +
                    "The response is a list of Tutorial objects with id, title, description and published status."
    )
    @GetMapping("/tutorials/published")
    public ResponseEntity<Map<String, Object>> findByPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            List<Tutorial> tutorials;
            Pageable pageable = PageRequest.of(page, size);
            Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true, pageable);

            tutorials = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalPages", pageTuts.getTotalPages());
            response.put("size", size);
            response.put("first", pageTuts.isFirst());
            response.put("last", pageTuts.isLast());
            response.put("empty", pageTuts.isEmpty());
            response.put("tutorials", tutorials);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
