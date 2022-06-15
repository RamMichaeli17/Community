package restapi.webapp.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import restapi.webapp.entities.UserEntity.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;

public class NameConvertor implements AttributeConverter<Name, String> {

    private final ObjectMapper objectMapper;

    @Autowired
    public NameConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Name name) {
        if (name == null) return null;
        return this.objectMapper.writeValueAsString(name);
    }

    @Override
    public Name convertToEntityAttribute(String name) {
        return null;
    }
}
