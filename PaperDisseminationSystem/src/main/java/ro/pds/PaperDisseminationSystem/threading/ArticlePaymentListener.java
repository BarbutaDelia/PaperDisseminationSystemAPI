package ro.pds.PaperDisseminationSystem.threading;

import org.web3j.protocol.core.DefaultBlockParameterName;
import redis.clients.jedis.Jedis;
import ro.pds.PaperDisseminationSystem.entities.Article;
import ro.pds.PaperDisseminationSystem.services.ArticleService;
import ro.pds.PaperDisseminationSystem.smartcontracts.ArticlePayment;

public class ArticlePaymentListener extends Thread{
    private final ArticlePayment contract;
    private final ArticleService articleService;
    private final Integer retryInterval;

    public ArticlePaymentListener(ArticlePayment contract, ArticleService articleService, Integer retryInterval) {
        this.contract = contract;
        this.articleService = articleService;
        this.retryInterval = retryInterval;
    }

    @Override
    public void run() {
        System.out.println("Running...");
        contract.articlePaidEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    System.out.println("Paid...");
                            Integer articleId = event.articleId.intValue();
                            Article article = articleService.getArticle(Long.valueOf(articleId));
                            article.setPaymentStatus(true);
                            articleService.saveArticle(article);
                            Jedis jedis = new Jedis("localhost");
                            jedis.del("--latest articles");
                            jedis.close();
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
