package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import co.com.pragma.usecase.reporte.in.ReportUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReporteUseCase implements ReportUseCasePort {

    private final ReporteRepository reporteRepository;

    @Override
    public Mono<Reporte> generarReporte() {
        return reporteRepository.generateReport();
    }
}
