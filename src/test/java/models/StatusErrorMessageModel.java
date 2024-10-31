package models;
import com.google.gson.annotations.Expose;
import lombok.Data;
@Data
public class StatusErrorMessageModel {
    @Expose
    private String status;
}