package sansan.sentix.Module.Articles.Response.SSI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsiSectorsDataRes {

    @JsonProperty("industryName")
    private IndustryName industryName;
    @JsonProperty("industryLevel")
    private Long industryLevel;

    @JsonProperty("listCompany")
    private List<Company> listCompany;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IndustryName {
        @JsonProperty("vi")
        private String vi;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Company {
        @JsonProperty("symbol")
        private String symbol;
    }

    public String buildCompany() {
        if (listCompany == null || listCompany.isEmpty()) {
            return "";
        }
        return listCompany.stream()
                .map(Company::getSymbol)
                .collect(Collectors.joining(";"));
    }


}


