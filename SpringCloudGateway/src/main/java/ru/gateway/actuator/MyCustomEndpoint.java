package ru.gateway.actuator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**Пример самописного эндпойнта для актуатора*/
@Component
@Endpoint(id = "myCustomEndpoint")
public class MyCustomEndpoint {
    MyCustomData data=new MyCustomData(101, "Hello");

    @ReadOperation //GET   http://localhost/actuator/myCustomEndpoint
    public MyCustomData getData() {
        return data;
    }
    @WriteOperation //POST
    public void writeData(Integer id, String msg) {
        data.i=id;
        data.s=msg;
    }
    @DeleteOperation //DELETE
    public Integer deleteData() {
        data.i=-1;
        data.s="";
        return 0;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class MyCustomData{
    Integer i;
    String s;
}