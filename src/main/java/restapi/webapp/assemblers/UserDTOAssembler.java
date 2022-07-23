package restapi.webapp.assemblers;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import restapi.webapp.dtos.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import java.util.Objects;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that add links to an UserDTO entity.
 */
@Component
public class UserDTOAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    /**
     * A method that gets an entity and returns an EntityModel of it,
     * in addition to adding specific links.
     * @param userDTO entity to be converted.
     * @return EntityModel of the UserDTO entity.
     */
    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO){
        return EntityModel.of(userDTO)
                .add(linkTo(methodOn(UserController.class).getAllUsersDtoInfo()).withRel("Get all users info"))
                .add(linkTo(methodOn(UserController.class).getUserDtoInfo
                        (Objects.requireNonNull(userDTO.getUser().getUserId()))).withSelfRel());
    }

    /**
     * A method that gets entities and returns a CollectionModel of them,
     * in addition to adding specific links.
     * @param entities entities to be converted.
     * @return CollectionModel of the entities.
     */
    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities){
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(UserController.class).getAllUsersDtoInfo()).withSelfRel());
    }
}