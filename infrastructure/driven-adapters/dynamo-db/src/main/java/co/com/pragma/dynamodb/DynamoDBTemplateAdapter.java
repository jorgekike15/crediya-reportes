package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;


@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Reporte,
        String, ReporteEntity > implements ReporteRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, DynamoDbAsyncClient dynamoDbAsyncClient) {
        super(connectionFactory, mapper, d -> mapper.map(d, Reporte.class), "Reportes");
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @Override
    public Mono<Reporte> generateReport() {
        return Mono.fromFuture(
                table.getItem(r -> r.key(Key.builder()
                        .partitionValue("1")
                        .build()))
        ).map(entity -> Reporte.builder()
                .id(entity.getId())
                .cantidadPrestamosAprobados(entity.getAtr1())
                .build());
    }

    @Override
    public Mono<Void> incrementarContador(String id, Double monto) {
        return Mono.fromFuture(
                dynamoDbAsyncClient.updateItem(builder -> builder
                        .tableName("Reportes")
                        .key(Map.of("id", AttributeValue.builder().s(id).build()))
                        .updateExpression(
                                "SET solicitudesAprobadas = if_not_exists(solicitudesAprobadas, :zero) + :inc, " +
                                        "montoSolicitudesAprobadas = if_not_exists(montoSolicitudesAprobadas, :zeroMonto) + :monto"
                        )
                        .expressionAttributeValues(Map.of(
                                ":inc", AttributeValue.builder().n("1").build(),
                                ":zero", AttributeValue.builder().n("0").build(),
                                ":monto", AttributeValue.builder().n(String.valueOf(monto)).build(),
                                ":zeroMonto", AttributeValue.builder().n("0").build()
                        ))
                )
        ).then();
    }
}
