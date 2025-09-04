package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ReporteResponseDTO;
import co.com.pragma.model.reporte.Reporte;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteDTOMapper {

    ReporteResponseDTO fromModel(Reporte reporte);
}
