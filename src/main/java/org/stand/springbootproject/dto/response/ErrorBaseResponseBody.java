package org.stand.springbootproject.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorBaseResponseBody extends BaseResponseBody {
    private HttpStatus status;

    public ErrorBaseResponseBody(HttpStatus status, Object message) {
        super(message);
        this.status = status;
    }
}
