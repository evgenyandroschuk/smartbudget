package smartbudget.view;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 24.12.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FaultResponse extends RuntimeException implements Serializable {

    private String reason;

    public FaultResponse(String reason) {
        this.reason = reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }


}
