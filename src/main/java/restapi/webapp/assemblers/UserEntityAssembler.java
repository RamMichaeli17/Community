package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import restapi.webapp.entities.UserEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that converts a user entity into an EntityModel<UserEntity>
 * or a CollectionModel<EntityModel<UserEntity>>.
 */
@Component
public class UserEntityAssembler implements RepresentationModelAssembler<UserEntity, EntityModel<UserEntity>> {

    /**
     * A method that gets a user entity and returns an EntityModel of it.
     * @param entity User entity to be converted.
     * @return EntityModel of the user entity.
     */
    @Override
    public EntityModel<UserEntity> toModel(UserEntity entity) {
        return EntityModel.of(entity)
                .add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users"))
                .add(linkTo(methodOn(UserController.class)
                        .getUserWithPathVar("id", entity.getUserId().toString())).withSelfRel());
    }

    /**
     * A method that gets a user entity and returns a CollectionModel of it.
     * @param entities User entities to be converted.
     * @return CollectionModel of the user entities.
     */
    @Override
    public CollectionModel<EntityModel<UserEntity>> toCollectionModel(Iterable<? extends UserEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add((linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users")));
    }
}