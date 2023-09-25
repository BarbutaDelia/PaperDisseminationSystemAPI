package ro.pds.PaperDisseminationSystem.threading;


import org.web3j.protocol.core.DefaultBlockParameterName;
import ro.pds.PaperDisseminationSystem.entities.Article;
import ro.pds.PaperDisseminationSystem.entities.Review;
import ro.pds.PaperDisseminationSystem.services.ArticleService;
import ro.pds.PaperDisseminationSystem.services.ReviewService;
import ro.pds.PaperDisseminationSystem.smartcontracts.ArticlePayment;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ReviewersPaymentListener extends Thread{
    private final ArticlePayment contract;
    private final ArticleService articleService;
    private final ReviewService reviewService;
    private final Integer retryInterval;
    private final Double articleUploadPrice;

    public ReviewersPaymentListener(ArticlePayment contract, ArticleService articleService, ReviewService reviewService, Integer retryInterval, Double articleUploadPrice) {
        this.contract = contract;
        this.articleService = articleService;
        this.reviewService = reviewService;
        this.retryInterval = retryInterval;
        this.articleUploadPrice = articleUploadPrice;
    }

    private Double convertWeiToEther(BigInteger amountWei) {
        BigDecimal amountEth = new BigDecimal(amountWei).divide(BigDecimal.TEN.pow(18));
        return amountEth.doubleValue();
    }

    @Override
    public void run() {
        System.out.println("Running reviewer thread...");
        contract.reviewerPaidEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                            System.out.println("Paid reviewers...");
                            Integer articleId = event.articleId.intValue();
                            Integer reviewId = event.reviewId.intValue();
                            BigInteger amountWei = event.amount;
//                            System.out.println(amountWei);
                            Double amountEther = convertWeiToEther(amountWei);
//                            System.out.println(amountWei);
                            Double grade = event.articleGrade.doubleValue();
                            Article article = articleService.getArticle(Long.valueOf(articleId));
                            Review review = reviewService.getReview(Long.valueOf(reviewId));
                            if(article.getGrade() == null) {
                                article.setGrade(grade);
                                articleService.saveArticle(article);
                            }
                            if(review.getEarnedAmount() == null){
                                review.setEarnedAmount(amountEther);
                                reviewService.saveReview(review);
                            }
                        },
                        error -> {
                            System.err.println("An error occurred: " + error.getMessage());
                        });

        while (true) {
            try {
                Thread.sleep(retryInterval * 1000);
            } catch (InterruptedException ex) {
//                    System.err.println(e.getMessage());
            }
        }
    }
}

