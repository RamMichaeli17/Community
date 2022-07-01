package restapi.webapp.factories;

import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import restapi.webapp.dtos.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that add links to an UserDTO entity.
 */
@Component
public class UserDTOAssembler implements SimpleRepresentationModelAssembler<UserDTO> {

    /**
     * A method that adds links to an entity of UserDTO.
     * @param resource Entity to add links to.
     */
    @Override
    public void addLinks(EntityModel<UserDTO> resource) {
        resource.add(linkTo(methodOn(UserController.class).getAllUsersInfo()).withRel("Get all users info"));
        resource.add(linkTo(methodOn(UserController.class).getUserInfo(resource.getContent().getUser().getUserId()))
                        .withSelfRel()
        );
    }

    /**
     * A method that adds links to a collection of entities of UserDTOs.
     * @param resources Entities to add links to.
     */
    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {
        resources.add(linkTo(methodOn(UserController.class).getAllUsersInfo()).withSelfRel());
    }
}