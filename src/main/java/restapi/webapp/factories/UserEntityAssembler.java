    package restapi.webapp.factories;

    import org.springframework.hateoas.CollectionModel;
    import org.springframework.hateoas.EntityModel;
    import org.springframework.hateoas.server.LinkRelationProvider;
    import org.springframework.hateoas.server.RepresentationModelAssembler;
    import org.springframework.stereotype.Component;
    import restapi.webapp.controllers.UserController;
    import restapi.webapp.entities.UserEntity;
    import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
    import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

    @Component

    public class UserEntityAssembler implements RepresentationModelAssembler<UserEntity, EntityModel<UserEntity>> {
        @Override
        public EntityModel<UserEntity> toModel(UserEntity entity) {
            return EntityModel.of(entity)
                    .add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users"))
                    .add(linkTo(methodOn(UserController.class).getUserByEmail(entity.getEmail())).withSelfRel());
        }

        @Override
        public CollectionModel<EntityModel<UserEntity>> toCollectionModel(Iterable<? extends UserEntity> entities) {
            return RepresentationModelAssembler.super.toCollectionModel(entities)
                    .add((linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users")));
        }
    }
