package ro.pds.PaperDisseminationSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.smartcontracts.ArticlePayment;
import ro.pds.PaperDisseminationSystem.smartcontracts.NFTContract;
import ro.pds.PaperDisseminationSystem.threading.NFTMintListener;
import ro.pds.PaperDisseminationSystem.threading.ArticlePaymentListener;
import ro.pds.PaperDisseminationSystem.services.ReviewerPaymentService;
import ro.pds.PaperDisseminationSystem.threading.ReviewersPaymentListener;

import java.math.BigInteger;

@SpringBootApplication
@EnableScheduling
public class PaperDisseminationSystemApplication implements CommandLineRunner {
	private final Credentials credentials;
	private final String articleContractAddress;
	private final String NFTContractAddress;
	private final String infuraUrl;
	private final Integer retryInterval;
	private final Double articleUploadPrice;
	private final ArticleService articleService;
	private final UserService userService;
	private final TagLevelService tagLevelService;
	private final UserTestService userTestService;
	private final ReviewService reviewService;

	@Autowired
	private ReviewerPaymentService reviewerPaymentService;


	public PaperDisseminationSystemApplication(@Value("${wallet.key}") String key, @Value("${articleSmartcontract.address}") String contractAddress, @Value("${NFTSmartcontract.address}") String NFTContractAddress, ArticleService articleService,  UserService userService, TagLevelService tagLevelService, ReviewService reviewService, UserTestService userTestService, @Value("${web3j.infura.endpoint}") String infuraUrl, @Value("${retry.interval}") Integer retryInterval, @Value("${articleUpload.price}") Double articleUploadPrice) {
		this.credentials = Credentials.create(key);
		this.articleContractAddress = contractAddress;
		this.NFTContractAddress = NFTContractAddress;
		this.infuraUrl = infuraUrl;
		this.retryInterval = retryInterval;
		this.articleUploadPrice = articleUploadPrice;
		this.articleService = articleService;
		this.userService = userService;
		this.tagLevelService = tagLevelService;
		this.userTestService = userTestService;
		this.reviewService = reviewService;
	}

	public static void main(String[] args) {
		SpringApplication.run(PaperDisseminationSystemApplication.class, args);
	}

	@Override
	public void run(String... args){
		Web3j infuraWeb3j = Web3j.build(new HttpService(infuraUrl));
		ArticlePayment contract = ArticlePayment.load(articleContractAddress, infuraWeb3j, credentials, new StaticGasProvider(BigInteger.valueOf(4_300_000_000L), BigInteger.valueOf(5_000_000L)));

		ArticlePaymentListener paymentListener = new ArticlePaymentListener(contract, articleService, retryInterval);
		paymentListener.start();

		NFTContract nftContract = NFTContract.load(NFTContractAddress, infuraWeb3j, credentials, new StaticGasProvider(BigInteger.valueOf(4_300_000_000L), BigInteger.valueOf(5_000_000L)));
		NFTMintListener NFTMintListener = new NFTMintListener(nftContract, userService, tagLevelService, userTestService, retryInterval);
		NFTMintListener.start();

		ReviewersPaymentListener reviewersPaymentListener = new ReviewersPaymentListener(contract, articleService, reviewService, retryInterval, articleUploadPrice);
		reviewersPaymentListener.start();
	}

}

