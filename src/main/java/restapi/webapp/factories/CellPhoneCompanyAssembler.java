package restapi.webapp.factories;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CellPhoneCompanyController;
import restapi.webapp.entities.CellPhoneCompany;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CellPhoneCompanyAssembler implements RepresentationModelAssembler<CellPhoneCompany,
        EntityModel<CellPhoneCompany>> {
    @Override
    public EntityModel<CellPhoneCompany> toModel(CellPhoneCompany entity) {
        return EntityModel.of(entity)
                .add(linkTo(methodOn(CellPhoneCompanyController.class).getAllCompanies()).withRel("Get all companies"))
                .add(linkTo(methodOn(CellPhoneCompanyController.class)
                        .getCompanyWithPathVar("name", entity.getCompanyName())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<CellPhoneCompany>> toCollectionModel
            (Iterable<? extends CellPhoneCompany> entities) {

        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add((linkTo(methodOn(CellPhoneCompanyController.class).getAllCompanies())
                        .withRel("Get all companies")));
    }
}
