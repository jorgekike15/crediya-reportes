package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReporteUseCaseTest {

    @Test
    void generarReporte_debeRetornarReporteDelRepositorio() {
        ReporteRepository mockRepo = mock(ReporteRepository.class);
        Reporte reporteEsperado = Reporte.builder().id("1").cantidadPrestamosAprobados(5).build();
        when(mockRepo.generateReport()).thenReturn(Mono.just(reporteEsperado));

        ReporteUseCase useCase = new ReporteUseCase(mockRepo);

        StepVerifier.create(useCase.generarReporte())
                .expectNext(reporteEsperado)
                .verifyComplete();

        verify(mockRepo).generateReport();
    }
}
