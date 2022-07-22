package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CellPhoneCompanyController;
import restapi.webapp.dtos.CellPhoneCompanyDTO;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class that converts a CellPhoneCompany entity into an EntityModel<CellPhoneCompany>
 * or a CollectionModel<EntityModel<CellPhoneCompany>>.
 */
@Component
public class CellPhoneCompanyDTOAssembler implements SimpleRepresentationModelAssembler<CellPhoneCompanyDTO> {

    /**
     * A method that adds links to an entity of CellPhoneCompanyDTO.
     * @param resource Entity to add links to.
     */
    @Override
    public void addLinks(EntityModel<CellPhoneCompanyDTO> resource) {
        resource.add(linkTo(methodOn(CellPhoneCompanyController.class)
                .getAllCompanies()).withRel("Get all companies info"));
        resource.add(linkTo(methodOn(CellPhoneCompanyController.class)
                .getCellPhoneCompanyDtoInfo(Objects.requireNonNull(resource.getContent())
                        .getCellPhoneCompany().getCellPhoneCompanyId())).withSelfRel()
        );
    }

    /**
     * A method that adds links to a collection of entities of CellPhoneCompanyDTOs.
     * @param resources Entities to add links to.
     */
    @Override
    public void addLinks(CollectionModel<EntityModel<CellPhoneCompanyDTO>> resources) {
        resources.add(linkTo(methodOn(CellPhoneCompanyController.class)
                .getAllCellPhoneCompaniesDtoInfo()).withSelfRel());
    }
}
