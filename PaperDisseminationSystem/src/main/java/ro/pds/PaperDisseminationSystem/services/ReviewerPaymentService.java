package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import ro.pds.PaperDisseminationSystem.entities.Article;
import ro.pds.PaperDisseminationSystem.entities.Review;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfArticlesNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfReviewsNotFound;
import ro.pds.PaperDisseminationSystem.smartcontracts.ArticlePayment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.abs;

@Service
public class ReviewerPaymentService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ReviewService reviewService;

    @Value("${articleSmartcontract.address}")
    private String contractAddress;

    @Value("${web3j.infura.endpoint}")
    private String infuraUrl;

    @Value("${wallet.key}")
    private String key;

    @Value("${articleUpload.price}")
    private Double articleUploadPrice;

    // Async method to pay reviewers
    public CompletableFuture<Void> payReviewersAsync() {
        // Get all articles with expired review periods
        try {
            List<Article> articlesWithExpiredReviewPeriods = articleService.listAllArticlesWithExpiredReviewPeriods();

            List<ArticlePayment.Review> reviews = new ArrayList<>();
            ArticlePayment articlePayment = getContractInstance();
            for (Article expiredArticle : articlesWithExpiredReviewPeriods) {
                try {
                    List<Review> articleReviews = reviewService.getReviewsByArticleId(expiredArticle.getId());
                    if (articleReviews.size() < 3) {
                        handleNotEnoughReviewsFound(articlesWithExpiredReviewPeriods);
                        // skip to the next article
                        continue;
                    }
                    for (Review review : articleReviews) {
                        ArticlePayment.Review reviewObject = getArticlePaymentReview(review, articleReviews);
                        reviews.add(reviewObject);
                    }
                } catch (CollectionOfReviewsNotFound e) {
                    handleNotEnoughReviewsFound(articlesWithExpiredReviewPeriods);
                }
            }

            // Call the payReviewers method using the contract wrapper
            return CompletableFuture.runAsync(() -> {
                try {
                    System.out.println("Sending data to smart contract...");
                    articlePayment.payReviewers(reviews).send();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });

        } catch (CollectionOfArticlesNotFound e) {
            System.out.println("There were no articles with expired review periods!");
            return CompletableFuture.completedFuture(null);
        }
    }

    private void handleNotEnoughReviewsFound(List<Article> articles) {
        System.out.println("There were not enough reviews for the article!");
        Article lastArticle = articles.get(articles.size() - 1);
        lastArticle.setReviewUntil(LocalDateTime.now().plusDays(30));
        articleService.saveArticle(lastArticle);
    }

    // Method which schedules the payment process to run at 00:00, every day
    @Scheduled(cron = "0 0 0 * * *")
    public void payReviewersScheduled() {
        try {
            CompletableFuture<Void> future = payReviewersAsync();
            // Wait for the payment process to complete, with a timeout
            future.get(30, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            System.out.println("The payment process took too long and timed out.");
        } catch (Exception e) {
            System.out.println("An error occurred while paying reviewers: " + e.getMessage());
        }
    }

    private ArticlePayment.Review getArticlePaymentReview(Review review, List<Review> articleReviews){
        BigInteger articleId = BigInteger.valueOf(review.getArticle().getId());
        BigInteger reviewId = BigInteger.valueOf(review.getId());
        Address reviewer = new Address(review.getUser().getMetamaskId());
        Double amountToPayReviewer = articleUploadPrice * getFractionOfMoneyForReviewer(review, articleReviews);
        BigInteger amount = convertEtherToWei(amountToPayReviewer);

        // Round the article grade to the nearest integer for the smart contract
        Double meanGrade = getReviewsMeanGrade(articleReviews);
        BigDecimal decimalValue = BigDecimal.valueOf(meanGrade).setScale(0, RoundingMode.HALF_UP);
        BigInteger articleGrade = decimalValue.toBigInteger();
        ArticlePayment.Review reviewObject = new ArticlePayment.Review(reviewId, articleId, String.valueOf(reviewer), amount, articleGrade);
        return reviewObject;
    }
    private ArticlePayment getContractInstance(){
        Web3j infuraWeb3j = Web3j.build(new HttpService(infuraUrl));
        // Create an instance of the contract wrapper
        Credentials credentials = Credentials.create(key);
        return ArticlePayment.load(contractAddress, infuraWeb3j, credentials, new StaticGasProvider(BigInteger.valueOf(4_300_000_000L), BigInteger.valueOf(5_000_000L)));
    }

    private Double getReviewsMeanGrade(List<Review> reviews){
        Double sum = 0D;
        for(Review review: reviews){
            sum += review.getArticleGrade();
        }
        return sum / reviews.size();
    }

    private Double getSumOfInverseDeviations(List<Review> reviews){
        Double sum = 0D;
        Double mean = getReviewsMeanGrade(reviews);
        for(Review review: reviews){
            sum += 1 / (abs(mean - review.getArticleGrade()) + 1);
        }
        return sum;
    }

    private Double getFractionOfMoneyForReviewer(Review review, List<Review> reviews){
        Double mean = getReviewsMeanGrade(reviews);
        return (1 / (abs(mean - review.getArticleGrade()) + 1)) * 1 / getSumOfInverseDeviations(reviews);
    }

    private BigInteger convertEtherToWei(double etherSum) {
        BigInteger weiConversion = BigInteger.TEN.pow(18);
        return BigDecimal.valueOf(etherSum).multiply(new BigDecimal(weiConversion)).toBigInteger();
    }

}

