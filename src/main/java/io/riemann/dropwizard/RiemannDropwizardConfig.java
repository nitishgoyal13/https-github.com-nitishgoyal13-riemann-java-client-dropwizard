package main.java.io.riemann.dropwizard;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

/***
 Created by nitish.goyal on 04/08/18
 ***/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiemannDropwizardConfig {

    @NotEmpty
    private String host;

    private int port = 5556;

    @NotEmpty
    private String prefix;

    @Max(3600)
    @Min(10)
    private int pollingInterval = 30;

    @Singular
    private List<String> tags = Collections.emptyList();
}
