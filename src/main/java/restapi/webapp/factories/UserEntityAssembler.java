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

public class UserEntityAssembler extends SimpleIdentifiableRepresentationModelAssembler<UserEntity> {
    public UserEntityAssembler() {
        super(UserController.class);
    }

    @Override
    public void addLinks(EntityModel<UserEntity> resource) {
        super.addLinks(resource);
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserEntity>> resources) {
        super.addLinks(resources);
        resources.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users"));
    }
}
