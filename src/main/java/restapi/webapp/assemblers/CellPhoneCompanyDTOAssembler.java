package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CellPhoneCompanyController;
import restapi.webapp.dtos.CellPhoneCompanyDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that converts a CellPhoneCompany entity into an EntityModel<CellPhoneCompany>
 * or a CollectionModel<EntityModel<CellPhoneCompany>>.
 */
@Component
public class CellPhoneCompanyDTOAssembler implements RepresentationModelAssembler<CellPhoneCompanyDTO,
        EntityModel<CellPhoneCompanyDTO>> {

    /**
     * A method that gets an entity and returns an EntityModel of it,
     * in addition to adding specific links.
     * @param entity entity to be converted.
     * @return EntityModel of the CellPhoneCompanyDTO entity.
     */
    @Override
    public EntityModel<CellPhoneCompanyDTO> toModel(CellPhoneCompanyDTO entity) {
        return EntityModel.of(entity)
                .add(linkTo(methodOn(CellPhoneCompanyController.class)
                .getAllCompanies()).withRel("Get all companies info"))
                .add(linkTo(methodOn(CellPhoneCompanyController.class)
                        .getCellPhoneCompanyDtoInfo
                                (entity.getCellPhoneCompany().getCellPhoneCompanyId())).withSelfRel());
    }

    /**
     * A method that gets entities and returns a CollectionModel of them,
     * in addition to adding specific links.
     * @param entities entities to be converted.
     * @return CollectionModel of the entities.
     */
    @Override
    public CollectionModel<EntityModel<CellPhoneCompanyDTO>> toCollectionModel
    (Iterable<? extends CellPhoneCompanyDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(CellPhoneCompanyController.class)
                        .getAllCellPhoneCompaniesDtoInfo()).withSelfRel());
    }
}
