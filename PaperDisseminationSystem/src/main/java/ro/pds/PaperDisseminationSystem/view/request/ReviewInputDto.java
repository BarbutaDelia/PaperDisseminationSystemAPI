package ro.pds.PaperDisseminationSystem.view.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInputDto {
    private BigInteger reviewId;
    private BigInteger articleId;
    private String reviewer;
    private BigInteger amount;
    private BigInteger articleGrade;
}
