package co.com.pragma.sqs.sender.aws;

import co.com.pragma.model.reporte.gateways.ReporteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsMessageListener {

    private final ReporteRepository reporteRepository;

    @SqsListener("${adapter.sqs.queue-contador-solicitudes-response}")
    public void receiveMessage(String messageBody) {
        log.info("Received message from SQS: {}", messageBody);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(messageBody);
            String decision = json.get("message").asText();
            Double monto = json.get("message2").asDouble();

            log.info("decision recibida: {}", decision);

            if ("APROBADA".equals(decision)) {
                String id = "1";
                log.info("Incrementando prestamos aprobados para el reporte ");

                reporteRepository.incrementarContador(id, monto)
                        .doOnSuccess(unused -> log.info("Contador actualizado en DynamoDB"))
                        .doOnError(error -> log.error("Error actualizando contador: {}", error.getMessage(), error))
                        .subscribe();
            }
        } catch (Exception e) {
            log.error("Error procesando mensaje de SQS reportes: {}", e.getMessage(), e);
        }
    }
}
