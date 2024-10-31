package models;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class SemaforoModel {
    @Expose(serialize = false)
    private Long id;
    @Expose
    private Double lat;
    @Expose
    private Double lng;
    @Expose
    private String status;
}

