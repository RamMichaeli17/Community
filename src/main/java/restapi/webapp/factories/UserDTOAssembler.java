package restapi.webapp.factories;

import restapi.webapp.dtos.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO dto) {
        return EntityModel.of(dto)
                .add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users"))
                .add(linkTo(methodOn(UserController.class)
                        .getUserWithPathVar("id", dto.getUser().getUserId().toString())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> dtos) {
        return RepresentationModelAssembler.super.toCollectionModel(dtos)
                .add((linkTo(methodOn(UserController.class).getAllUsers()).withRel("Get all users")));
    }

}