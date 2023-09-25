package ro.pds.PaperDisseminationSystem.threading;

import org.web3j.protocol.core.DefaultBlockParameterName;
import ro.pds.PaperDisseminationSystem.entities.TagLevel;
import ro.pds.PaperDisseminationSystem.entities.User;
import ro.pds.PaperDisseminationSystem.entities.UserTest;
import ro.pds.PaperDisseminationSystem.exceptions.TagLevelNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.services.TagLevelService;
import ro.pds.PaperDisseminationSystem.services.UserService;
import ro.pds.PaperDisseminationSystem.services.UserTestService;
import ro.pds.PaperDisseminationSystem.smartcontracts.NFTContract;

public class NFTMintListener extends Thread{
    private final NFTContract contract;
    private final UserService userService;
    private final TagLevelService tagLevelService;
    private final UserTestService userTestService;
    private final Integer retryInterval;

    public NFTMintListener(NFTContract contract, UserService userService, TagLevelService tagLevelService, UserTestService userTestService, Integer retryInterval) {
        this.contract = contract;
        this.userService = userService;
        this.tagLevelService = tagLevelService;
        this.userTestService = userTestService;
        this.retryInterval = retryInterval;
    }

    @Override
    public void run() {
        try {
            System.out.println("Running NFT thread");
            contract.nFTMintedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
                    .subscribe(event -> {
                        System.out.println("Paid NFT ...");
                        String metamaskAddress = event.owner;//event.recipient
                        String tokenURI = event.tokenURI;
                        updateReceivedBadge(metamaskAddress, tokenURI);
                    }, error -> {
                        System.err.println("An error occurred: " + error.getMessage());
                    });
        } catch (Exception e) {
            System.err.println("Failed to subscribe to NFTMinted events: " + e.getMessage());
            return;
        }
        while (true) {
            try {
                Thread.sleep(retryInterval * 1000);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }


    public void updateReceivedBadge(String metamaskAddress, String tokenUri){
        String CID = tokenUri.split("ipfs://")[1];
        try {
            User user = userService.getUserByMetamaskId(metamaskAddress);
            TagLevel tagLevel = tagLevelService.getTagLevelByCID(CID);
            UserTest userTest = userTestService.getLatestUserTestByUserIdAndTagId(user.getId(), tagLevel.getId());
            if(userTest != null) {
                userTest.setReceivedBadge(true);
                userTestService.saveUserTest(userTest);
            }
        }
        catch(UserNotFound | TagLevelNotFound e){
            System.out.println(e.getMessage());
        }
    }

}
