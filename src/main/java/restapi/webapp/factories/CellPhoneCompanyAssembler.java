package restapi.webapp.factories;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CellPhoneCompanyController;
import restapi.webapp.entities.CellPhoneCompany;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that converts a CellPhoneCompany entity into an EntityModel<CellPhoneCompany>
 * or a CollectionModel<EntityModel<CellPhoneCompany>>
 */
@Component
public class CellPhoneCompanyAssembler implements RepresentationModelAssembler<CellPhoneCompany,
        EntityModel<CellPhoneCompany>> {

    /**
     * A method that gets a cell phone company entity and returns an EntityModel of it.
     * @param entity Entity to be converted.
     * @return EntityModel of the cell phone company
     */
    @Override
    public EntityModel<CellPhoneCompany> toModel(CellPhoneCompany entity) {
        return EntityModel.of(entity)
                .add(linkTo(methodOn(CellPhoneCompanyController.class).getAllCompanies()).withRel("Get all companies"))
                .add(linkTo(methodOn(CellPhoneCompanyController.class)
                        .getCompanyWithPathVar("name", entity.getCompanyName())).withSelfRel());
    }

    /**
     * A method that gets a cell phone company entity and returns a CollectionModel of it.
     * @param entities Entities to be converted.
     * @return CollectionModel of the cell phone companies
     */
    @Override
    public CollectionModel<EntityModel<CellPhoneCompany>> toCollectionModel
            (Iterable<? extends CellPhoneCompany> entities) {

        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add((linkTo(methodOn(CellPhoneCompanyController.class).getAllCompanies())
                        .withRel("Get all companies")));
    }
}
