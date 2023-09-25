package ro.pds.PaperDisseminationSystem.smartcontracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class ArticlePayment extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_PAYFORARTICLE = "payForArticle";

    public static final String FUNC_PAYREVIEWERS = "payReviewers";

    public static final String FUNC_ARTICLEPAYMENTS = "articlePayments";

    public static final String FUNC_GETSTOREDREVIEW = "getStoredReview";

    public static final String FUNC_GETTOTALPAIDTOREVIEWER = "getTotalPaidToReviewer";

    public static final String FUNC_REVIEWERPAYMENTS = "reviewerPayments";

    public static final String FUNC_STOREDREVIEWS = "storedReviews";

    public static final Event ARTICLEPAID_EVENT = new Event("ArticlePaid", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event REVIEWERPAID_EVENT = new Event("ReviewerPaid", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected ArticlePayment(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArticlePayment(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ArticlePayment(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ArticlePayment(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ArticlePaidEventResponse> getArticlePaidEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTICLEPAID_EVENT, transactionReceipt);
        ArrayList<ArticlePaidEventResponse> responses = new ArrayList<ArticlePaidEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ArticlePaidEventResponse typedResponse = new ArticlePaidEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.articleId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.userAddress = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArticlePaidEventResponse> articlePaidEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArticlePaidEventResponse>() {
            @Override
            public ArticlePaidEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ARTICLEPAID_EVENT, log);
                ArticlePaidEventResponse typedResponse = new ArticlePaidEventResponse();
                typedResponse.log = log;
                typedResponse.articleId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.userAddress = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArticlePaidEventResponse> articlePaidEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTICLEPAID_EVENT));
        return articlePaidEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> payForArticle(BigInteger articleId, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYFORARTICLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(articleId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> payReviewers(List<Review> reviews) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYREVIEWERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Review>(Review.class, reviews)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<ReviewerPaidEventResponse> getReviewerPaidEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REVIEWERPAID_EVENT, transactionReceipt);
        ArrayList<ReviewerPaidEventResponse> responses = new ArrayList<ReviewerPaidEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReviewerPaidEventResponse typedResponse = new ReviewerPaidEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.reviewer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.articleId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.reviewId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.articleGrade = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReviewerPaidEventResponse> reviewerPaidEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReviewerPaidEventResponse>() {
            @Override
            public ReviewerPaidEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REVIEWERPAID_EVENT, log);
                ReviewerPaidEventResponse typedResponse = new ReviewerPaidEventResponse();
                typedResponse.log = log;
                typedResponse.reviewer = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.articleId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.reviewId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.articleGrade = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReviewerPaidEventResponse> reviewerPaidEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REVIEWERPAID_EVENT));
        return reviewerPaidEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> articlePayments(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ARTICLEPAYMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<StoredReview> getStoredReview(BigInteger reviewId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTOREDREVIEW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(reviewId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StoredReview>() {}));
        return executeRemoteCallSingleValueReturn(function, StoredReview.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalPaidToReviewer(String reviewer) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTOTALPAIDTOREVIEWER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, reviewer)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> reviewerPayments(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_REVIEWERPAYMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> storedReviews(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STOREDREVIEWS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    @Deprecated
    public static ArticlePayment load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArticlePayment(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ArticlePayment load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArticlePayment(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ArticlePayment load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ArticlePayment(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ArticlePayment load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ArticlePayment(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class Review extends StaticStruct {
        public BigInteger reviewId;

        public BigInteger articleId;

        public String reviewer;

        public BigInteger amount;

        public BigInteger articleGrade;

        public Review(BigInteger reviewId, BigInteger articleId, String reviewer, BigInteger amount, BigInteger articleGrade) {
            super(new org.web3j.abi.datatypes.generated.Uint256(reviewId), 
                    new org.web3j.abi.datatypes.generated.Uint256(articleId), 
                    new org.web3j.abi.datatypes.Address(160, reviewer), 
                    new org.web3j.abi.datatypes.generated.Uint256(amount), 
                    new org.web3j.abi.datatypes.generated.Uint256(articleGrade));
            this.reviewId = reviewId;
            this.articleId = articleId;
            this.reviewer = reviewer;
            this.amount = amount;
            this.articleGrade = articleGrade;
        }

        public Review(Uint256 reviewId, Uint256 articleId, Address reviewer, Uint256 amount, Uint256 articleGrade) {
            super(reviewId, articleId, reviewer, amount, articleGrade);
            this.reviewId = reviewId.getValue();
            this.articleId = articleId.getValue();
            this.reviewer = reviewer.getValue();
            this.amount = amount.getValue();
            this.articleGrade = articleGrade.getValue();
        }
    }

    public static class StoredReview extends StaticStruct {
        public String reviewer;

        public BigInteger articleGrade;

        public StoredReview(String reviewer, BigInteger articleGrade) {
            super(new org.web3j.abi.datatypes.Address(160, reviewer), 
                    new org.web3j.abi.datatypes.generated.Uint256(articleGrade));
            this.reviewer = reviewer;
            this.articleGrade = articleGrade;
        }

        public StoredReview(Address reviewer, Uint256 articleGrade) {
            super(reviewer, articleGrade);
            this.reviewer = reviewer.getValue();
            this.articleGrade = articleGrade.getValue();
        }
    }

    public static class ArticlePaidEventResponse extends BaseEventResponse {
        public BigInteger articleId;

        public BigInteger amount;

        public String userAddress;
    }

    public static class ReviewerPaidEventResponse extends BaseEventResponse {
        public String reviewer;

        public BigInteger articleId;

        public BigInteger reviewId;

        public BigInteger amount;

        public BigInteger articleGrade;
    }
}
