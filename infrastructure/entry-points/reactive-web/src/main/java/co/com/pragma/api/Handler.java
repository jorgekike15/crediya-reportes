package co.com.pragma.api;

import co.com.pragma.api.dto.ReporteResponseDTO;
import co.com.pragma.api.mapper.ReporteDTOMapper;
import co.com.pragma.usecase.reporte.in.ReportUseCasePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ReportUseCasePort reportUseCasePort;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final ResourceBundle bundle = ResourceBundle.getBundle("log4j2");
    private final ReporteDTOMapper reporteDTOMapper;

    public Mono<ServerResponse> listenGETReport(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETReport"));
        }
        return ServerResponse.ok().body(
                reportUseCasePort.generarReporte()
                        .map(reporteDTOMapper::fromModel), ReporteResponseDTO.class)
                .doOnError(e -> log.error("Error al consultar el reporte", e))
                .doFinally(signalType ->
                        log.info("Fin de método: listenGETReport (señal: {})", signalType));
    }

}
