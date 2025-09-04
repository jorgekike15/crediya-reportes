package co.com.pragma.dynamodb;


import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data
@Builder
@DynamoDbBean
public class ReporteEntity {

    private String id;
    private int atr1;

    public ReporteEntity() {
    }

    public ReporteEntity(String id, int atr1) {
        this.id = id;
        this.atr1 = atr1;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("solicitudesAprobadas")
    public int getAtr1() {
        return atr1;
    }

    public void setAtr1(int atr1) {
        this.atr1 = atr1;
    }
}
